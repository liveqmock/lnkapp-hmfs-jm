package org.fbi.hmfsjm.gateway.domain.base.xml;

import java.io.Serializable;

public class ToaHeader implements Serializable {
    public String TXN_CODE = "";                  // 交易请求码
    public String REQ_SN = "";                    // 流水号 客户端应用系统内唯一
    public String RET_CODE = "";                  // 交易响应码
    public String ERR_MSG = "";                   // 交易响应信息
}
