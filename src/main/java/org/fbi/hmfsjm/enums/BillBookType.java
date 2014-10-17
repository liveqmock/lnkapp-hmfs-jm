package org.fbi.hmfsjm.enums;

import java.util.Hashtable;

/**
 * 缴款单记账类型 00-收款  10-退款 20-支取 99-开户
 */
public enum BillBookType implements EnumApp {

    OPEN("99", "开户"),
    DEPOSIT("00", "收款"),
    DEPOSIT_AGAIN("30", "续缴"),
    REFUND("10", "退款"),
    DRAW("20", "支取"),
    INTEREST_DRAW_CURRENT("80", "支取时活期利息"),
    INTEREST_SCHE_CURRENT("90", "正常活期利息"),
    INTEREST_SCHE_FIXED("91", "正常三个月定期利息");

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

    public String typeToQryDetail() {
        if ("00".equals(this.code)) return "0001";
        if ("30".equals(this.code)) return "0004";
        if ("20".equals(this.code)) return "0003";
        if ("10".equals(this.code)) return "0002";
        if ("80".equals(this.code)) return "0005";
        if ("90".equals(this.code)) return "0005";
        if ("91".equals(this.code)) return "0006";
        return "";
    }
}
