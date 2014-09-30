package org.fbi.hmfsjm.gateway.domain.base.xml;

import java.io.Serializable;

public class TiaHeader implements Serializable {
    public String TXN_CODE = "";                   // 交易请求码
    public String REQ_SN = "";                     // 流水号 客户端应用系统内唯一
}
