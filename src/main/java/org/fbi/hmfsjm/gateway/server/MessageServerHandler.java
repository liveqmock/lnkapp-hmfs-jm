package org.fbi.hmfsjm.gateway.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.lang.StringUtils;
import org.fbi.hmfsjm.enums.TxnRtnCode;
import org.fbi.hmfsjm.gateway.domain.base.Tia;
import org.fbi.hmfsjm.gateway.domain.base.Toa;
import org.fbi.hmfsjm.gateway.domain.base.xml.TiaHeader;
import org.fbi.hmfsjm.gateway.domain.base.xml.ToaHeader;
import org.fbi.hmfsjm.helper.MD5Helper;
import org.fbi.hmfsjm.online.action.AbstractTxnAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class MessageServerHandler extends SimpleChannelInboundHandler<String> {
    private static final Logger logger = LoggerFactory.getLogger(MessageServerHandler.class);
    private static Map<String,Object> contextsMap = new ConcurrentHashMap<String,Object>();

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String requestBuffer) throws Exception {
        String responseBuffer = "";
        logger.info("服务器收到报文：" + requestBuffer);
        byte[] bytes = requestBuffer.getBytes();
        String reqsn = "";
        String wsysid = "";
        String txnCode = "";
        String txnDate = "";
        String rtnMsgHeader = "";
        Toa toa = null;
        try {
            // 解析报文头
            byte[] headerBytes = new byte[64];
            System.arraycopy(bytes, 0, headerBytes, 0, headerBytes.length);
            String msgHeader = new String(headerBytes, "GBK");
            // 解析报文体
            byte[] bodyBytes = new byte[bytes.length - 64];
            System.arraycopy(bytes, 64, bodyBytes, 0, bodyBytes.length);
            String msgData = new String(bodyBytes, "GBK");
            // 返回报文初始化
            rtnMsgHeader = msgHeader.substring(0, 32);

            // 外围系统代码、交易码、交易日期、mac
            wsysid = msgHeader.substring(4, 14).trim().toUpperCase();
            txnCode = msgHeader.substring(14, 24).trim();
            txnDate = msgHeader.substring(24, 32).trim();
            String mac = msgHeader.substring(32);

            // MD5校验
            // Message Data部分加上8位交易日期加上用户ID后产生的MD5值
            String md5 = MD5Helper.getMD5String(msgData + txnDate + wsysid);
            // 验证失败 返回验证失败信息
            if (!md5.equals(mac)) {
                logger.info("MD5校验不一致。[服务端]:" + md5 + " [客户端]:" + mac);
                throw new RuntimeException("MD5校验错误");
            }

            Class tiaClazz = Class.forName("org.fbi.hmfsjm.gateway.domain.txn.Tia" + txnCode);
            Tia tia = (Tia) tiaClazz.newInstance();
            tia = tia.getTia(msgData);
            TiaHeader tiaHeader = (TiaHeader) tiaClazz.getField("INFO").get(tia);
            reqsn = tiaHeader.REQ_SN;

            Class txnClass = Class.forName("org.fbi.hmfsjm.online.action.Txn" + txnCode + "Action");
            AbstractTxnAction action = (AbstractTxnAction)txnClass.newInstance();
            toa = action.run(tia);

        } catch (Exception e) {
            // 返回异常信息
            String exmsg = e.getMessage();
            if (exmsg == null) exmsg = TxnRtnCode.OTHER_EXCEPTION.toRtnMsg();
            else if (!exmsg.contains("|")) exmsg = TxnRtnCode.OTHER_EXCEPTION.getCode() + "|" + exmsg;
            logger.error(exmsg, e);
            String[] arrExmsg = StringUtils.splitByWholeSeparatorPreserveAllTokens(exmsg, "|");
            Class toaClazz = Class.forName("org.fbi.hmfsjm.gateway.domain.txn.Toa" + txnCode);
            toa = (Toa) toaClazz.newInstance();
            ToaHeader toaHeader = new ToaHeader();
            toaHeader.REQ_SN = reqsn;
            toaHeader.RET_CODE = arrExmsg[0];
            toaHeader.ERR_MSG = arrExmsg[1];
            toaClazz.getField("INFO").set(toa, toaHeader);

        }
        String rtnMsgData = toa.toString();
        String txnMac = MD5Helper.getMD5String(rtnMsgData + txnDate + wsysid);
        rtnMsgHeader += txnMac;
        responseBuffer = rtnMsgHeader + rtnMsgData;
        ctx.writeAndFlush(responseBuffer);
        ctx.close();
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("ChannelInactived.");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("Unexpected exception from downstream.", cause);
        ctx.close();
    }

}
