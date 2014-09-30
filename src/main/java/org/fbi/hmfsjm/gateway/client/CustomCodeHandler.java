package org.fbi.hmfsjm.gateway.client;

import org.fbi.hmfsjm.gateway.domain.base.Tia;
import org.fbi.hmfsjm.gateway.domain.base.Toa;
import org.fbi.hmfsjm.helper.MD5Helper;
import org.fbi.hmfsjm.helper.ProjectConfigManager;
import org.fbi.hmfsjm.helper.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 编码解码
 */
public class CustomCodeHandler {

    private static Logger logger = LoggerFactory.getLogger(CustomCodeHandler.class);

    public static byte[] encode(Tia tia) {
        String msgdata = tia.toString();
        String wsysid = ProjectConfigManager.getInstance().getProperty("server.wsysid");
        String txnDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String txnCode = getSubstrBetweenStrs(msgdata, "<TXN_CODE>", "</TXN_CODE>");
        String mac = MD5Helper.getMD5String(msgdata + txnDate + wsysid);

        String msg = "1.00" + StringHelper.rightPad4ChineseToByteLength(wsysid, 10, " ")
                + StringHelper.rightPad4ChineseToByteLength(txnCode, 10, " ")
                + txnDate + mac + msgdata;
        int msglength = msg.getBytes().length + 8;
        String len = StringHelper.rightPad4ChineseToByteLength("" + msglength, 8, " ");

        byte[] bytes = new byte[msglength];
        System.arraycopy(len.getBytes(), 0, bytes, 0, 8);
        System.arraycopy(msg.getBytes(), 0, bytes, 8, msglength - 8);
        logger.info("[LNKAPP-HMFS-JM客户端]发送报文：" + msg);
        return bytes;
    }

    public static Toa decode(byte[] bytes) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        logger.info("[LNKAPP-HMFS-JM 客户端接收]" + new String(bytes));

        String wsysid = new String(bytes, 4, 10).trim();
        String txnCode = new String(bytes, 14, 10).trim();
        String txnDate = new String(bytes, 24, 8).trim();
        String mac = new String(bytes, 32, 32);
        String msgdata = new String(bytes, 64, bytes.length - 64);
        String md5 = MD5Helper.getMD5String(msgdata + txnDate + wsysid);
        logger.info("交易日期：" + txnDate + " 系统ID:" + wsysid);
        if (!md5.equalsIgnoreCase(mac)) {
            logger.error("校验失败,MAC不一致.本地生成md5:" + md5 + " 服务端mac:" + mac);
            throw new RuntimeException("报文校验失败,MAC不一致");
        }
        Class clazz = Class.forName("org.fbi.hmfsjm.gateway.domain.txn.Toa" + txnCode);
        Toa tmptoa = (Toa) clazz.newInstance();
        Toa toa = tmptoa.toBean(msgdata);
        return toa;
    }

    static String getSubstrBetweenStrs(String fromStr, String startStr, String endStr) {
        int length = startStr.length();
        int start = fromStr.indexOf(startStr) + length;
        int end = fromStr.indexOf(endStr);
        return fromStr.substring(start, end);
    }
}
