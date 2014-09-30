package org.fbi.hmfsjm.enums;

import java.util.Hashtable;

/**
 * 票据凭证状态 凭证状态(1-领用、2-使用、3-作废)
 */
public enum VoucherStatus implements EnumApp {

    CHECK("1", "领用"),
    USED("2", "使用"),
    CANCEL("3", "作废");

    private String code = null;
    private String title = null;
    private static Hashtable<String, VoucherStatus> aliasEnums;

    VoucherStatus(String code, String title) {
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

    public static VoucherStatus valueOfAlias(String alias) {
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
