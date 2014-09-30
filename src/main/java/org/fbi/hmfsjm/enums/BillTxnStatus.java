package org.fbi.hmfsjm.enums;

import java.util.Hashtable;

/**
 * 缴款确认返回码
 */
public enum BillTxnStatus implements EnumApp {

    PAYED_SECCESS("0000", "缴存收款确认成功"),
    NO_EXIST("0001", "没有此缴存通知单"),
    UNCHECKED("0002", "缴存通知单处于受理状态"),
    BANK_PAYED("0003", "缴存通知单银行已经完成收款"),
    CANCELED("0004", "缴存通知单已经作废"),
    UNCONNECT("0005", "尚未联网，不能执行收款确认"),
    CONFIRMED("0006", "缴存通知单已收款确认");

    private String code = null;
    private String title = null;
    private static Hashtable<String, BillTxnStatus> aliasEnums;

    BillTxnStatus(String code, String title) {
        this.init(code, title);
    }

    @SuppressWarnings("unchecked")
    private void init(String code, String title) {
        this.code = code;
        this.title = title;
        synchronized (this.getClass()) {
            if (aliasEnums == null) {
                aliasEnums = new Hashtable();
            }
        }
        aliasEnums.put(code, this);
        aliasEnums.put(title, this);
    }

    public static BillTxnStatus valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }

    public String toRtnMsg() {
        return this.code + "|" + this.title;
    }
}
