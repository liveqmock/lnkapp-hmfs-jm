package org.fbi.hmfsjm.online.service;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.fbi.hmfsjm.enums.ActStatus;
import org.fbi.hmfsjm.enums.BillBookType;
import org.fbi.hmfsjm.enums.SendFlag;
import org.fbi.hmfsjm.enums.TxnRtnCode;
import org.fbi.hmfsjm.gateway.client.SyncSocketClient;
import org.fbi.hmfsjm.gateway.domain.txn.Tia5002;
import org.fbi.hmfsjm.gateway.domain.txn.Toa5002;
import org.fbi.hmfsjm.helper.ObjectFieldsCopier;
import org.fbi.hmfsjm.helper.ProjectConfigManager;
import org.fbi.hmfsjm.repository.MybatisManager;
import org.fbi.hmfsjm.repository.dao.CommonMapper;
import org.fbi.hmfsjm.repository.dao.HmfsJmActMapper;
import org.fbi.hmfsjm.repository.dao.HmfsJmActTxnMapper;
import org.fbi.hmfsjm.repository.dao.HmfsJmInterestMapper;
import org.fbi.hmfsjm.repository.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 利息
 */
public class InterestService {

    private static final Logger logger = LoggerFactory.getLogger(InterestService.class);
    MybatisManager manager = new MybatisManager();
    private ActirtService actirtService = new ActirtService();

    // 计息
    public void accrualInterest() throws IllegalAccessException {
        String sdf = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        logger.info("开始计息时间:" + sdf);
        String today = sdf.substring(0, 8);
        String time = sdf.substring(8);
        SqlSession session = null;
        BigDecimal curRate = actirtService.qryCurrentRate();       // 活期日利率
        BigDecimal fixedRate = actirtService.qry3MonthRate();      // 定期3个月年利率
        try {
            session = manager.getSessionFactory().openSession();
            HmfsJmActTxnMapper actTxnMapper = session.getMapper(HmfsJmActTxnMapper.class);
            HmfsJmActMapper actMapper = session.getMapper(HmfsJmActMapper.class);
            HmfsJmActExample example = new HmfsJmActExample();
            logger.info(today.substring(4));
            example.createCriteria().andIntDateLike("%" + today.substring(4))
                    .andActStatusEqualTo(ActStatus.VALID.getCode())
                    .andBalAmtGreaterThan(new BigDecimal("0.00"));
            List<HmfsJmAct> acts = actMapper.selectByExample(example);
            logger.info("计息账户数：" + acts.size());
            for (HmfsJmAct act : acts) {
                logger.info("计息账户：" + act.getHouseAccount());
                // 计息
                // 上次存款日和计息日的差小于一年，则取活期利率
                int days = actirtService.daysBetween(act.getLastDepDate(), today);
                if (days == 0) {
                    continue;     // 无需计息，计算下一账户
                }
                // 计息记账流水
                CommonMapper cmnMapper = session.getMapper(CommonMapper.class);
                String maxSerialNo = cmnMapper.qryMaxSerialNo(act.getHouseAccount());
                String interestSerialNo = getInterestNo(maxSerialNo);
                HmfsJmActTxn inttxn = new HmfsJmActTxn();
                // 计息提交
                HmfsJmInterest interest = new HmfsJmInterest();

                ObjectFieldsCopier.copyFields(act, inttxn);
                inttxn.setPkid(UUID.randomUUID().toString());
                inttxn.setActSerialNo(interestSerialNo);
                inttxn.setTxnCode("5002");
                inttxn.setOperDate(today);
                inttxn.setOperTime(time);
                boolean isAfterOneYear = actirtService.isAfterOneYear(act.getLastDepDate(), today);
                BigDecimal interAmt = null;
                if (isAfterOneYear) { // 定期
                    interAmt = act.getBalAmt().multiply(fixedRate);
                    interest.setRate(fixedRate.toString());
                    inttxn.setBookType(BillBookType.INTEREST_SCHE_FIXED.getCode());
                } else {
                    interAmt = act.getBalAmt().multiply(curRate);
                    interest.setRate(curRate.toString());
                    inttxn.setBookType(BillBookType.INTEREST_SCHE_CURRENT.getCode());
                }
                inttxn.setTxnAmt(interAmt);
                actTxnMapper.insert(inttxn);

                interest.setPkid(UUID.randomUUID().toString());
                interest.setInterest(interAmt);
                interest.setInterestNo("JX" + act.getHouseAccount() + interestSerialNo);
                interest.setHouseId(act.getHouseId());
                interest.setHouseAccount(act.getHouseAccount());
                interest.setHouseLocation(act.getHouseLocation());
                interest.setOwner(act.getOwner());
                interest.setBeforeAmt(act.getBalAmt());     // 结息前资金指结息前余额
                BigDecimal nowBal = act.getBalAmt().add(interAmt);
                interest.setAfterAmt(nowBal);               // 结息后资金指结息后余额
                interest.setBeginDate(toDate10(act.getIntDate()));    // 计息日
                interest.setEndDate(toDate10(today));  // 交易日
                interest.setCapital(act.getBalAmt());
                interest.setOperTime(time);
                interest.setOperId("9999");
                interest.setSerialNo(interestSerialNo);
                interest.setTxnCode("5002");
                interest.setIntName("年度结息");
                interest.setDeptId("010");
                interest.setSendFlag(SendFlag.UNSEND.getCode());
                HmfsJmInterestMapper interestMapper = session.getMapper(HmfsJmInterestMapper.class);
                interestMapper.insert(interest);
                // 更新账户
                act.setBalAmt(act.getBalAmt().add(interAmt));
                act.setIntAmt(act.getIntAmt().add(interAmt));
                act.setIntDate(today);
                actMapper.updateByPrimaryKey(act);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
            throw new RuntimeException(e);
        } finally {
            if (session != null) session.close();
        }
    }

    // 发送计息明细(定时每天发送未发送成功的计息记录)
    public void sendInterestTxns() {
        SqlSession session = null;
        int pkgCnt = Integer.parseInt(ProjectConfigManager.getInstance().getProperty("interest.pkg.details"));
        session = manager.getSessionFactory().openSession();
        HmfsJmInterestMapper interestMapper = session.getMapper(HmfsJmInterestMapper.class);
        HmfsJmInterestExample example = new HmfsJmInterestExample();
        example.createCriteria().andSendFlagEqualTo(SendFlag.UNSEND.getCode());
        List<HmfsJmInterest> interests = interestMapper.selectByExample(example);
        if (interests.isEmpty()) {
            logger.info("发送笔数：0");
            session.close();
            return;
        }
        if (pkgCnt >= interests.size()) {
            sendInterestList(interests);
        } else {
            int num = interests.size() / pkgCnt + 1;
            if (interests.size() % pkgCnt == 0) num--;
            List<HmfsJmInterest> list = null;
            for (int i = 0; i < num; i++) {
                if (i == (num - 1)) {
                    list = interests.subList(pkgCnt * i, interests.size());
                } else {
                    list = interests.subList(pkgCnt * i, pkgCnt * (i + 1));
                }
                sendInterestList(list);
            }
        }
        session.close();
    }

    private void sendInterestList(List<HmfsJmInterest> list) {
        SqlSession session = null;
        String sendtime = new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(new Date());
        try {
            Tia5002 tia = new Tia5002();
            tia.INFO.REQ_SN = new SimpleDateFormat("yyyyMMddHHmmsssss").format(new Date());

            for (HmfsJmInterest record : list) {
                Tia5002.Detail detail = new Tia5002.Detail();
                detail.HOUSE_ACCOUNT = record.getHouseAccount();
                detail.INTEREST_NO = record.getInterestNo();
                detail.INTEREST_NAME = record.getIntName();
                detail.HOUSE_ID = record.getHouseId();
                detail.HOUSE_LOCATION = record.getHouseLocation();
                detail.OWNER = record.getOwner();
                detail.BEGIN_DATE = record.getBeginDate();
                detail.END_DATE = record.getEndDate();
                detail.BEFORE_AMT = record.getBeforeAmt().toString();
                detail.AFTER_AMT = record.getAfterAmt().toString();
                detail.CAPITAL = record.getCapital().toString();
                detail.RATE = record.getRate().toString();
                detail.INTEREST = record.getInterest().toString();
                tia.BODY.DETAILS.add(detail);
            }

            Toa5002 toa = (Toa5002) new SyncSocketClient().onRequest(tia);
            if (toa == null) throw new RuntimeException("网络异常");
            if (TxnRtnCode.TXN_SECCESS.getCode().equals(toa.BODY.STS_CODE)) {

                session = manager.getSessionFactory().openSession();
                HmfsJmInterestMapper interestMapper = session.getMapper(HmfsJmInterestMapper.class);
                for (HmfsJmInterest record : list) {
                    record.setSendDatetime(sendtime);
                    record.setSendFlag(SendFlag.SENT.getCode());
                    interestMapper.updateByPrimaryKey(record);
                }
                session.commit();
            } else {
                logger.info("发送计息返回结果：" + toa.BODY.STS_CODE);
            }
        } catch (Exception e) {
            session.rollback();
            throw new RuntimeException("利息发送异常", e);
        } finally {
            if (session != null) session.close();
        }
    }

    private String toDate10(String date8) {
        return date8.substring(0, 4) + "-" + date8.substring(4, 6) + "-" + date8.substring(6, 8);
    }

    private String getInterestNo(String maxNo) {
        if(StringUtils.isEmpty(maxNo)) return "000001";
        else {
            int num = Integer.parseInt(maxNo);
            num++;
            return StringUtils.leftPad(String.valueOf(num), 6, "0");
        }
    }
}


