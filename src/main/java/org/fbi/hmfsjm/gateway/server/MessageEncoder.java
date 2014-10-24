package org.fbi.hmfsjm.gateway.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.fbi.hmfsjm.helper.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: zhanrui
 * Date: 13-4-13
 */

public class MessageEncoder  extends MessageToByteEncoder<String> {
    private static final Logger logger = LoggerFactory.getLogger(MessageEncoder.class);
    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
        logger.info("[LNKAPP-HMFS-JM 响应报文内容：]" + msg);
        byte[] data = msg.getBytes("GBK");
        byte[] msglen = (StringHelper.rightPad4ChineseToByteLength("" + (data.length + 8), 8, " ")).getBytes();

        byte[] bytesResData = new byte[data.length + 8];
        System.arraycopy(msglen, 0, bytesResData, 0, msglen.length);
        System.arraycopy(data, 0, bytesResData, msglen.length, data.length);
        out.writeBytes(bytesResData);

    }
}
