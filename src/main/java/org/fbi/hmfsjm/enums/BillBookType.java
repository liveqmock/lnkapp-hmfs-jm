package org.fbi.hmfsjm.enums;

import java.util.Hashtable;

/**
 * �ɿ�������� 00-�տ�  10-�˿� 20-֧ȡ 99-����
 */
public enum BillBookType implements EnumApp {

    OPEN("99", "����"),
    DEPOSIT("00", "�տ�"),
    REFUND("10", "�˿�"),
    DRAW("20", "֧ȡ");

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
