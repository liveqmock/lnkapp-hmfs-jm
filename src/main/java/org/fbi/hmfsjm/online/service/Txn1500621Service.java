package org.fbi.hmfsjm.online.service;

import org.fbi.hmfsjm.enums.BillBookType;
import org.fbi.hmfsjm.enums.BillStsFlag;
import org.fbi.hmfsjm.gateway.client.SyncSocketClient;
import org.fbi.hmfsjm.gateway.domain.base.Toa;
import org.fbi.hmfsjm.gateway.domain.txn.Tia3002;
import org.fbi.hmfsjm.gateway.domain.txn.Toa3002;
import org.fbi.hmfsjm.helper.ObjectFieldsCopier;
import org.fbi.hmfsjm.repository.MybatisManager;
import org.fbi.hmfsjm.repository.dao.HmfsJmActMapper;
import org.fbi.hmfsjm.repository.dao.HmfsJmActTxnMapper;
import org.fbi.hmfsjm.repository.dao.HmfsJmRefundMapper;
import org.fbi.hmfsjm.repository.model.*;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 1500621 �˿�ȷ�� ҵ���߼�
 */
public class Txn1500621Service {

    private static final Logger logger = LoggerFactory.getLogger(Txn1500621Service.class);
    private RefundService refundService = new RefundService();
    MybatisManager manager = new MybatisManager();

    public Toa process(String tellerID, String branchID, String serialNo, String refundNo, String txnDate) {

        Tia3002 tia = new Tia3002();
        tia.BODY.REFUND_BILLNO = refundNo;
        tia.INFO.REQ_SN = new SimpleDateFormat("yyyyMMddHHmmsssss").format(new Date());
        HmfsJmRefund refund = refundService.qryRefundByNo(refundNo);
        if (refund == null) {
            throw new RuntimeException("�˿�Ų�����:" + refundNo);
        }
        refund.setCfmTxnCode("3002");
        refund.setActSerialNo(serialNo);
        refund.setOperId(tellerID);
        refund.setDeptId(branchID);
        refund.setStsFlag(BillStsFlag.BOOKED.getCode());
        SqlSession session = null;
        try {
            session = manager.getSessionFactory().openSession();
            HmfsJmRefundMapper refundMapper = session.getMapper(HmfsJmRefundMapper.class);
            refundMapper.updateByPrimaryKey(refund);
            HmfsJmActMapper actMapper = session.getMapper(HmfsJmActMapper.class);
            HmfsJmActExample actExample = new HmfsJmActExample();
            actExample.createCriteria().andHouseAccountEqualTo(refund.getHouseAccount());
            List<HmfsJmAct> actList = actMapper.selectByExample(actExample);
            if (actList.size() == 0) {
                logger.error("�˿�ֻ��˺Ų����ڣ��ֻ���:" + refund.getHouseAccount() + " �˿�ţ�" + refund.getBillno());
                throw new RuntimeException("�ֻ��˺Ų�����:" + refund.getHouseAccount() + " �˿�ţ�" + refund.getBillno());
            } else {   // �˿�
                HmfsJmAct act = actList.get(0);
                act.setBalAmt(act.getBalAmt().subtract(refund.getTxnAmt()));
                if (new BigDecimal("0.00").compareTo(act.getBalAmt()) > 0) {
                    throw new RuntimeException("�ֻ�����");
                }
                actMapper.updateByPrimaryKey(act);
                logger.info("�����³ɹ����ֻ���:" + refund.getHouseAccount() + " ���ţ�" + refund.getBillno());
            }
            HmfsJmActTxn txn = new HmfsJmActTxn();
            ObjectFieldsCopier.copyFields(refund, txn);
            txn.setPkid(UUID.randomUUID().toString());
            txn.setActSerialNo(serialNo);
            txn.setTxnCode("3002");
            txn.setOperDate(txnDate.substring(0, 8));
            txn.setOperTime(txnDate.substring(8));
            txn.setBookType(BillBookType.REFUND.getCode());
            HmfsJmActTxnMapper acttxnMapper = session.getMapper(HmfsJmActTxnMapper.class);
            acttxnMapper.insert(txn);
            logger.info("[3002-�˿�ȷ��-����] ��ˮ�ţ�" + tia.INFO.REQ_SN + " ���ţ�" + tia.BODY.REFUND_BILLNO);
            // ���׷���
            Toa3002 toa = (Toa3002) new SyncSocketClient().onRequest(tia);
            if (toa == null) throw new RuntimeException("�����쳣��");

            logger.info("[3002-�˿�ȷ��-��Ӧ] ��ˮ�ţ�" + toa.INFO.REQ_SN +
                    " ���ţ�" + toa.BODY.REFUND_BILLNO +
                    " ״̬�룺" + toa.BODY.BILL_STS_CODE +
                    " ״̬˵����" + toa.BODY.BILL_STS_TITLE);
            session.commit();
            return toa;
        } catch (Exception e) {
            session.rollback();
            String errmsg = e.getMessage();
            if (StringUtils.isEmpty(errmsg)) {
                throw new RuntimeException("�˿�ʧ��");
            } else
                throw new RuntimeException(errmsg);
        } finally {
            if (session != null) session.close();
        }
    }
}