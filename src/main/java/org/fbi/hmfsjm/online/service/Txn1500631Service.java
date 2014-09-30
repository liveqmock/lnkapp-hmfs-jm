package org.fbi.hmfsjm.online.service;

import apps.hmfsjm.enums.VoucherStatus;
import apps.hmfsjm.repository.MybatisManager;
import apps.hmfsjm.repository.dao.HmfsJmBillMapper;
import apps.hmfsjm.repository.dao.HmfsJmVoucherMapper;
import apps.hmfsjm.repository.model.HmfsJmBill;
import apps.hmfsjm.repository.model.HmfsJmBillExample;
import apps.hmfsjm.repository.model.HmfsJmVoucher;
import apps.hmfsjm.repository.model.HmfsJmVoucherExample;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 1500631 票据使用和作废
 */
public class Txn1500631Service {

    private static final Logger logger = LoggerFactory.getLogger(Txn1500631Service.class);
    MybatisManager manager = new MybatisManager();

    public boolean process(String branchID, String tellerID, String vchNo, String billNo, String vchflag) {

        if (VoucherStatus.USED.getCode().equals(vchflag)) {
            return processVchUsed(branchID, tellerID, vchNo, billNo);
        } else if (VoucherStatus.CANCEL.getCode().equals(vchflag)) {
            return processVchCancel(branchID, tellerID, vchNo);
        } else {
            throw new RuntimeException("票据状态错误");
        }
    }

    private boolean processVchUsed(String branchID, String tellerID, String vchNo, String billNo) {

        SqlSession session = null;
        try {
            session = manager.getSessionFactory().openSession();
            HmfsJmVoucherMapper vchMapper = session.getMapper(HmfsJmVoucherMapper.class);
            HmfsJmVoucherExample example = new HmfsJmVoucherExample();
            example.createCriteria().andVchNumEqualTo(vchNo).andVchStsEqualTo(VoucherStatus.CHECK.getCode());
            List<HmfsJmVoucher> vchList = vchMapper.selectByExample(example);

            HmfsJmBillMapper billMapper = session.getMapper(HmfsJmBillMapper.class);
            HmfsJmBillExample billExample = new HmfsJmBillExample();
            example.createCriteria().andBillnoEqualTo(billNo);
            List<HmfsJmBill> billList = billMapper.selectByExample(billExample);

            if (vchList == null || vchList.isEmpty())
                throw new RuntimeException("没有查询到票据号" + vchNo);
            else if (billList == null || billList.isEmpty()) {
                throw new RuntimeException("没有查询到缴款单号" + billNo);
            } else {
                HmfsJmVoucher vch = vchList.get(0);
                HmfsJmBill bill = billList.get(0);
                vch.setBillno(billNo);
                vch.setVchSts(VoucherStatus.USED.getCode());
                vch.setVchType(bill.getBookType());
                vch.setVchNum(vchNo);
                vch.setDeptId(branchID);
                vch.setOperId(tellerID);
                vch.setActTxnSn(bill.getActSerialNo());
                vch.setTxnDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
                vch.setTxnTime(new SimpleDateFormat("HHmmss").format(new Date()));
                if (vchMapper.updateByPrimaryKey(vch) == 1) {
                    session.commit();
                    return true;
                } else return false;
            }
        } finally {
            if (session != null) session.close();
        }
    }

    private boolean processVchCancel(String branchID, String tellerID, String vchNo) {
        SqlSession session = null;
        try {
            session = manager.getSessionFactory().openSession();
            HmfsJmVoucherMapper vchMapper = session.getMapper(HmfsJmVoucherMapper.class);
            HmfsJmVoucherExample example = new HmfsJmVoucherExample();
            example.createCriteria().andVchNumEqualTo(vchNo);
            List<HmfsJmVoucher> vchList = vchMapper.selectByExample(example);
            if (vchList == null || vchList.isEmpty())
                throw new RuntimeException("没有查询到票据号" + vchNo);
            else {
                HmfsJmVoucher vch = vchList.get(0);
                vch.setVchSts(VoucherStatus.CANCEL.getCode());
                vch.setVchNum(vchNo);
                vch.setDeptId(branchID);
                vch.setOperId(tellerID);
                vch.setTxnDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
                vch.setTxnTime(new SimpleDateFormat("HHmmss").format(new Date()));
                if (vchMapper.updateByPrimaryKey(vch) == 1) {
                    session.commit();
                    return true;
                } else return false;
            }
        } finally {
            if (session != null) session.close();
        }
    }
}
