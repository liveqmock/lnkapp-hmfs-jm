package org.fbi.hmfsjm.enums;

import java.util.Hashtable;

/**
 * 退款单查询返回码
 */
public enum RefundQryStatus implements EnumApp {

    VALAID("0000", "退款通知单有效"),
    NO_EXIST("0001", "没有此退款通知单"),
    HANDLING("0002", "退款通知单处于受理状态"),
    ALREADY_REFUND("0003", "已经完成退款确认"),
    CANCELED("0004", "退款通知单已经作废");

    private String code = null;
    private String title = null;
    private static Hashtable<String, RefundQryStatus> aliasEnums;

    RefundQryStatus(String code, String title) {
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

    public static RefundQryStatus valueOfAlias(String alias) {
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
