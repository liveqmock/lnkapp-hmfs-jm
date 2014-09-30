package org.fbi.hmfsjm.online.service;

import apps.hmfsjm.enums.BillBookType;
import apps.hmfsjm.enums.BillStsFlag;
import apps.hmfsjm.gateway.client.SyncSocketClient;
import apps.hmfsjm.gateway.domain.base.Toa;
import apps.hmfsjm.gateway.domain.txn.Tia2001;
import apps.hmfsjm.gateway.domain.txn.Toa2001;
import apps.hmfsjm.repository.MybatisManager;
import apps.hmfsjm.repository.dao.HmfsJmActMapper;
import apps.hmfsjm.repository.dao.HmfsJmActTxnMapper;
import apps.hmfsjm.repository.dao.HmfsJmBillMapper;
import apps.hmfsjm.repository.model.HmfsJmAct;
import apps.hmfsjm.repository.model.HmfsJmActExample;
import apps.hmfsjm.repository.model.HmfsJmActTxn;
import apps.hmfsjm.repository.model.HmfsJmBill;
import common.utils.ObjectFieldsCopier;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 1500611 �ɿ�ɿ�ȷ�� ҵ���߼� ÿ���ɿ�Ŷ�Ӧ�ķֻ�����ֻ��һ��������ʱ�轫ԭ�ֻ�ɾ�������¿���
 */
public class Txn1500611Service {

    private static final Logger logger = LoggerFactory.getLogger(Txn1500611Service.class);
    private BillService billService = new BillService();
    MybatisManager manager = new MybatisManager();

    public Toa process(String tellerID, String branchID, String serialNo, String billNo) {

        Tia2001 tia = new Tia2001();
        tia.BODY.PAY_BILLNO = billNo;
        tia.INFO.REQ_SN = new SimpleDateFormat("yyyyMMddHHmmsssss").format(new Date());
        HmfsJmBill bill = billService.qryBillByNo(billNo);
        if (bill == null) {
            throw new RuntimeException("�ɿ�Ų�����:" + billNo);
        }
        bill.setCfmTxnCode("2001");
        bill.setActSerialNo(serialNo);
        bill.setOperId(tellerID);
        bill.setDeptId(branchID);
        bill.setStsFlag(BillStsFlag.BOOKED.getCode());
        SqlSession session = null;
        try {
            session = manager.getSessionFactory().openSession();
            HmfsJmBillMapper mapper = session.getMapper(HmfsJmBillMapper.class);
            mapper.updateByPrimaryKey(bill);
            HmfsJmActMapper actMapper = session.getMapper(HmfsJmActMapper.class);
            HmfsJmActExample actExample = new HmfsJmActExample();
            actExample.createCriteria().andHouseAccountEqualTo(bill.getHouseAccount());
            List<HmfsJmAct> actList = actMapper.selectByExample(actExample);
            if (actList.size() == 0) {   // ����
                HmfsJmAct act = new HmfsJmAct();
                ObjectFieldsCopier.copyFields(bill, act);
                act.setBalAmt(bill.getTxnAmt());
                act.setIntAmt(new BigDecimal("0.00"));
                act.setMngAmt(new BigDecimal("0.00"));
                actMapper.insert(act);
                logger.info("�ֻ������ɹ����ֻ���:" + bill.getHouseAccount() + " �ɿ�ţ�" + bill.getBillno());
                // ������ϸ
            } else {   // �ѿ�������Ϊ����.
                HmfsJmAct act = actList.get(0);
                act.setBalAmt(act.getBalAmt().add(bill.getTxnAmt()));
                actMapper.updateByPrimaryKey(act);
                logger.info("�����³ɹ����ֻ���:" + bill.getHouseAccount() + " ���ţ�" + bill.getBillno());
            }
            HmfsJmActTxn txn = new HmfsJmActTxn();
            ObjectFieldsCopier.copyFields(bill, txn);
            txn.setActSerialNo(serialNo);
            txn.setTxnCode("2001");
            txn.setBookType(BillBookType.DEPOSIT.getCode());
            HmfsJmActTxnMapper acttxnMapper = session.getMapper(HmfsJmActTxnMapper.class);
            acttxnMapper.insert(txn);
            logger.info("[2001-�ɿ�ȷ��-����] ��ˮ�ţ�" + tia.INFO.REQ_SN + " ���ţ�" + tia.BODY.PAY_BILLNO);
            // ���׷���
            Toa2001 toa = (Toa2001) new SyncSocketClient().onRequest(tia);
            if (toa == null) throw new RuntimeException("�����쳣��");

            logger.info("[2001-�ɿ�ȷ��-��Ӧ] ��ˮ�ţ�" + toa.INFO.REQ_SN +
                    " ���ţ�" + toa.BODY.PAY_BILLNO +
                    " ״̬�룺" + toa.BODY.BILL_STS_CODE +
                    " ״̬˵����" + toa.BODY.BILL_STS_TITLE);
            session.commit();
            return toa;
        } catch (Exception e) {
            session.rollback();
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
}