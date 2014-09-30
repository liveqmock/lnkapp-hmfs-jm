package org.fbi.hmfsjm.gateway.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 报文解码
 */
public class MessageDecoder  extends ByteToMessageDecoder {
    private static final Logger logger = LoggerFactory.getLogger(MessageDecoder.class);
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {

        if (in.readableBytes() < 8) {
            return;
        }

        byte[] lengthBuffer = new byte[8];
        in.getBytes(0,lengthBuffer);

        int dataLength = Integer.parseInt(new String(lengthBuffer).trim());
        if (in.readableBytes() < dataLength) {
            return;
        }

        byte[] decoded = new byte[dataLength];
        in.readBytes(decoded);
        String msg = null;
        try {
            msg = new String(decoded, "GBK");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("报文解码字符集错误.", e);
        }
        logger.info("[LNKAPP-HMFS-JM 接收报文内容：]" + msg);
        out.add(msg);
    }
}
