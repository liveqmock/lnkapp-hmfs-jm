package org.fbi.hmfsjm.repository.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.fbi.hmfsjm.repository.model.VoucherBill;

import java.util.List;

/*
 * Õ®”√
 */
public interface CommonMapper {

    @Select(" select b.billno, b.txnamt, v.txndate, v.vchnum, v.vchsts" +
            " from " +
            " (select billno, txn_amt as txnamt from HMFS_JM_BILL where oper_date = #{date8}) b" +
            " full join " +
            " (select billno, txn_date as txndate, vch_num as vchnum, vch_sts as vchsts from hmfs_jm_voucher" +
            " where txn_date = #{date8} and vch_sts != '1') v" +
            " on b.billno = v.billno" +
            " order by billno,vchnum,vchsts")
    List<VoucherBill> qryVoucherBills(@Param("date8") String date8);

    @Select("select v.vch_num as vchnum, v.vch_sts as vchsts, b.billno as billno, " +
            " b.oper_date as txndate, b.txn_amt as txnamt from hmfs_jm_bill b  " +
            " left join hmfs_jm_voucher v" +
            " on b.billno = v.billno " +
            " where b.billno = #{billNo}")
    List<VoucherBill> qryVoucher(@Param("billNo") String billNo);

    @Select("select  count(v.vch_num) from hmfs_jm_voucher v " +
            " where v.txn_date = #{date8} and v.vch_sts = #{vchsts} ")
    String qryVchCnt(@Param("date8") String date8, @Param("vchsts") String vchsts);
}