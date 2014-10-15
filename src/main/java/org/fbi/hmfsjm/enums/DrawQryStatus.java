package org.fbi.hmfsjm.enums;

import java.util.Hashtable;

/**
 * 支取单查询返回码
 */
public enum DrawQryStatus implements EnumApp {

    VALAID("0000", "支取单有效"),
    NO_EXIST("0001", "没有此支取单"),
    HANDLING("0002", "支取单处于房管局审核状态"),
    ALREADY_DRAW("0003", "已经完成支取确认"),
    CANCELED("0004", "该支取单已经作废");

    private String code = null;
    private String title = null;
    private static Hashtable<String, DrawQryStatus> aliasEnums;

    DrawQryStatus(String code, String title) {
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

    public static DrawQryStatus valueOfAlias(String alias) {
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
