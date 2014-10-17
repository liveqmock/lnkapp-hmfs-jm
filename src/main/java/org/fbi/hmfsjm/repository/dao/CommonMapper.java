package org.fbi.hmfsjm.repository.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.fbi.hmfsjm.repository.model.HmTotalAct;
import org.fbi.hmfsjm.repository.model.VoucherBill;

import java.math.BigDecimal;
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

    @Select(" select max(serial_No) from hmfs_jm_interest t" +
            " where t.house_account = #{houseAct}")
    String qryMaxSerialNo(@Param("houseAct") String houseAct);

    @Select(" select count(t.house_id) hcnt, sum(t.house_area) sumArea, sum(t.bal_amt) sumAmt from HMFS_JM_ACT t" +
            " where t.act_status = '0000'")
    HmTotalAct qryTotalActInfo();

    @Select(" select sum(t.txn_amt) txnamt from hmfs_jm_act_txn t " +
            " where t.book_type in (#{type})")
    BigDecimal qrySumAmtByType(@Param("type") String type);
}