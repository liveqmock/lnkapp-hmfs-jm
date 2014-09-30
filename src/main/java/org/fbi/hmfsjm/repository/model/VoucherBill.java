package org.fbi.hmfsjm.repository.model;

/**
 * 票据使用情况
 */
public class VoucherBill {
    private String vchnum;
    private String vchsts;
    private String billno;
    private String txndate;
    private String txnamt;

    public String getVchnum() {
        return vchnum;
    }

    public void setVchnum(String vchnum) {
        this.vchnum = vchnum;
    }

    public String getVchsts() {
        return vchsts;
    }

    public void setVchsts(String vchsts) {
        this.vchsts = vchsts;
    }

    public String getBillno() {
        return billno;
    }

    public void setBillno(String billno) {
        this.billno = billno;
    }

    public String getTxndate() {
        return txndate;
    }

    public void setTxndate(String txndate) {
        this.txndate = txndate;
    }

    public String getTxnamt() {
        return txnamt;
    }

    public void setTxnamt(String txnamt) {
        this.txnamt = txnamt;
    }
}
