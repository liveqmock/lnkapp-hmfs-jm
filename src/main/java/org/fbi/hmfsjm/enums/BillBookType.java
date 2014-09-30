package org.fbi.hmfsjm.enums;

import java.util.Hashtable;

/**
 * 缴款单记账类型 00-收款  10-退款 20-支取 99-开户
 */
public enum BillBookType implements EnumApp {

    OPEN("99", "开户"),
    DEPOSIT("00", "收款"),
    REFUND("10", "退款"),
    DRAW("20", "支取");

    private String code = null;
    private String title = null;
    private static Hashtable<String, BillBookType> aliasEnums;

    BillBookType(String code, String title) {
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

    public static BillBookType valueOfAlias(String alias) {
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
