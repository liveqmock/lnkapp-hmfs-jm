package org.fbi.hmfsjm.enums;

import java.util.Hashtable;

/**
 * 缴款单查询返回码
 */
public enum BillQryStatus implements EnumApp {

    VALAID("0000", "缴存通知单有效"),
    NO_EXIST("0001", "没有此缴存通知单"),
    HANDLING("0002", "缴存通知单处于受理状态"),
    ALREADY_PAYED("0003", "已经完成收款确认"),
    CANCELED("0004", "缴存通知单已经作废");

    private String code = null;
    private String title = null;
    private static Hashtable<String, BillQryStatus> aliasEnums;

    BillQryStatus(String code, String title) {
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

    public static BillQryStatus valueOfAlias(String alias) {
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
