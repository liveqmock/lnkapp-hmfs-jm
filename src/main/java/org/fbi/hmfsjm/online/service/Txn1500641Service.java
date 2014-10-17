package org.fbi.hmfsjm.online.service;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.fbi.hmfsjm.enums.BillBookType;
import org.fbi.hmfsjm.enums.BillStsFlag;
import org.fbi.hmfsjm.enums.SendFlag;
import org.fbi.hmfsjm.gateway.client.SyncSocketClient;
import org.fbi.hmfsjm.gateway.domain.base.Toa;
import org.fbi.hmfsjm.gateway.domain.txn.Tia3004;
import org.fbi.hmfsjm.gateway.domain.txn.Toa3004;
import org.fbi.hmfsjm.helper.ObjectFieldsCopier;
import org.fbi.hmfsjm.repository.MybatisManager;
import org.fbi.hmfsjm.repository.dao.*;
import org.fbi.hmfsjm.repository.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 1500641 ֧ȡȷ�� ҵ���߼�
 */
public class Txn1500641Service {

    private static final Logger logger = LoggerFactory.getLogger(Txn1500641Service.class);
    private DrawService drawService = new DrawService();
    private ActirtService actirtService = new ActirtService();
    MybatisManager manager = new MybatisManager();

    public Toa process(String tellerID, String branchID, String serialNo, String drawNo, String txnDate) {

        Tia3004 tia = new Tia3004();
        tia.BODY.DRAW_BILLNO = drawNo;
        tia.INFO.REQ_SN = new SimpleDateFormat("yyyyMMddHHmmsssss").format(new Date());
        HmfsJmDraw draw = drawService.qryDrawByNo(drawNo);
        if (draw == null) {
            throw new RuntimeException("֧ȡ���Ų�����:" + drawNo);
        }
        draw.setCfmTxnCode("3004");
        draw.setActSerialNo(serialNo);
        draw.setOperId(tellerID);
        draw.setDeptId(branchID);
        draw.setStsFlag(BillStsFlag.BOOKED.getCode());
        SqlSession session = null;
        try {
            session = manager.getSessionFactory().openSession();
            HmfsJmDrawMapper drawMapper = session.getMapper(HmfsJmDrawMapper.class);
            drawMapper.updateByPrimaryKey(draw);
            HmfsJmActMapper actMapper = session.getMapper(HmfsJmActMapper.class);
            HmfsJmActExample actExample = new HmfsJmActExample();
            actExample.createCriteria().andHouseAccountEqualTo(draw.getHouseAccount());
            List<HmfsJmAct> actList = actMapper.selectByExample(actExample);
            if (actList.size() == 0) {
                logger.error("֧ȡ�ֻ��˺Ų����ڣ��ֻ���:" + draw.getHouseAccount() + " ֧ȡ���ţ�" + draw.getBillno());
                throw new RuntimeException("�ֻ��˺Ų�����:" + draw.getHouseAccount() + " ֧ȡ���ţ�" + draw.getBillno());
            } else {
                // ֧ȡ
                HmfsJmAct act = actList.get(0);
                String houseAct = act.getHouseAccount();

                // ֧ȡ��ˮ
                HmfsJmActTxn txn = new HmfsJmActTxn();
                ObjectFieldsCopier.copyFields(draw, txn);
                txn.setPkid(UUID.randomUUID().toString());
                txn.setActSerialNo(serialNo);
                txn.setTxnCode("3004");
                txn.setOperDate(txnDate.substring(0, 8));
                txn.setOperTime(txnDate.substring(8));
                txn.setBookType(BillBookType.DRAW.getCode());
                HmfsJmActTxnMapper acttxnMapper = session.getMapper(HmfsJmActTxnMapper.class);
                acttxnMapper.insert(txn);
                logger.info("[3004-֧ȡȷ��-����] ��ˮ�ţ�" + tia.INFO.REQ_SN + " ���ţ�" + tia.BODY.DRAW_BILLNO);
                // ��Ϣ������ˮ
                CommonMapper cmnMapper = session.getMapper(CommonMapper.class);
                String interestSerialNo = cmnMapper.qryMaxSerialNo(houseAct);
                if(StringUtils.isEmpty(interestSerialNo)) interestSerialNo = "000001";
                HmfsJmActTxn inttxn = new HmfsJmActTxn();
                ObjectFieldsCopier.copyFields(draw, inttxn);
                inttxn.setPkid(UUID.randomUUID().toString());
                inttxn.setActSerialNo(interestSerialNo);
                inttxn.setTxnCode("5002");
                inttxn.setOperDate(txnDate.substring(0, 8));
                inttxn.setOperTime(txnDate.substring(8));
                BigDecimal curRate = actirtService.qryPerDayCurrentRate();    // ����������
                BigDecimal interAmt = txn.getTxnAmt().multiply(curRate)
                        .multiply(new BigDecimal(actirtService.daysBetween(inttxn.getOperDate(), act.getIntDate())));
                inttxn.setBookType(BillBookType.INTEREST_DRAW_CURRENT.getCode());
                inttxn.setTxnAmt(interAmt);
                acttxnMapper.insert(inttxn);

                // ��Ϣ�ύ
                HmfsJmInterest interest = new HmfsJmInterest();
                interest.setPkid(UUID.randomUUID().toString());
                interest.setInterest(interAmt);
                interest.setInterestNo("JX" + houseAct + interestSerialNo);
                interest.setHouseId(act.getHouseId());
                interest.setHouseAccount(act.getHouseAccount());
                interest.setHouseLocation(act.getHouseLocation());
                interest.setOwner(act.getOwner());
                interest.setBeforeAmt(act.getBalAmt());     // ��Ϣǰ�ʽ�ָ��Ϣǰ���
                BigDecimal nowBal = act.getBalAmt().subtract(draw.getTxnAmt()).add(interAmt);
                interest.setAfterAmt(nowBal);               // ��Ϣ���ʽ�ָ��Ϣ�����
                interest.setBeginDate(act.getIntDate());    // ��Ϣ��
                interest.setEndDate(inttxn.getOperDate());  // ������
                interest.setCapital(draw.getTxnAmt());
                interest.setRate(curRate);
                interest.setOperTime(txnDate);
                interest.setOperId(tellerID);
                interest.setSerialNo(interestSerialNo);
                interest.setTxnCode("5002");
                interest.setIntName("֧ȡ��Ϣ");
                interest.setDeptId(branchID);
                interest.setSendFlag(SendFlag.UNSEND.getCode());
                HmfsJmInterestMapper interestMapper = session.getMapper(HmfsJmInterestMapper.class);
                interestMapper.insert(interest);
                // �����˻�
                act.setBalAmt(act.getBalAmt().subtract(draw.getTxnAmt()));
                act.setIntAmt(act.getIntAmt().add(interAmt));
                if (new BigDecimal("0.00").compareTo(act.getBalAmt()) > 0) {
                    throw new RuntimeException("�ֻ�����");
                }
                actMapper.updateByPrimaryKey(act);
                logger.info("�����³ɹ����ֻ���:" + draw.getHouseAccount() + " ���ţ�" + draw.getBillno());

                logger.info("[3004-֧ȡȷ��-����] ��ˮ�ţ�" + tia.INFO.REQ_SN + " ���ţ�" + tia.BODY.DRAW_BILLNO);

                // ���׷���
                Toa3004 toa = (Toa3004) new SyncSocketClient().onRequest(tia);
                if (toa == null) throw new RuntimeException("�����쳣��");

                logger.info("[3004-֧ȡȷ��-��Ӧ] ��ˮ�ţ�" + toa.INFO.REQ_SN +
                        " ���ţ�" + toa.BODY.DRAW_BILLNO +
                        " ״̬�룺" + toa.BODY.BILL_STS_CODE +
                        " ״̬˵����" + toa.BODY.BILL_STS_TITLE);
                session.commit();
                return toa;
            }
        } catch (Exception e) {
            session.rollback();
            String errmsg = e.getMessage();
            if (StringUtils.isEmpty(errmsg)) {
                throw new RuntimeException("֧ȡʧ��");
            } else
                throw new RuntimeException(errmsg);
        } finally {
            if (session != null) session.close();
        }
    }
}