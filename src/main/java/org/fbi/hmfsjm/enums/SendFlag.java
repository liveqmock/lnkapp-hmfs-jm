package org.fbi.hmfsjm.enums;

import java.util.Hashtable;

/**
 * 发送标志 0-未发送 1-已发送
 */
public enum SendFlag implements EnumApp {

    UNSEND("0", "未发送"),
    SENT("1", "已发送"),
    SENT_ERROR("2", "发送失败");

    private String code = null;
    private String title = null;
    private static Hashtable<String, SendFlag> aliasEnums;

    SendFlag(String code, String title) {
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

    public static SendFlag valueOfAlias(String alias) {
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
