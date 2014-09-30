package org.fbi.hmfsjm.gateway.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * User: zhanrui
 * Date: 13-4-13
 */
public class MessageServerHandler extends SimpleChannelInboundHandler<String> {
    private static final Logger logger = LoggerFactory.getLogger(MessageServerHandler.class);
    private static Map<String,Object> contextsMap = new ConcurrentHashMap<String,Object>();

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String requestBuffer) throws Exception {
        String responseBuffer = "";
        logger.info("服务器收到报文：" + requestBuffer);

        // TODO 业务交易执行

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
