package com.emis.vi.bm.javase.rpc.netty2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class EchoClientInboundHandler extends SimpleChannelInboundHandler {
    /**
     * 我们在Channel连接到远程节点直接发送一条消息给服务器
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello, Netty!", CharsetUtil.UTF_8));
    }

    /*@Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        // 输出从服务器Echo的消息
        System.out.printf("Client received: %s \n", byteBuf.toString(CharsetUtil.UTF_8));
    }*/

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        // 输出从服务器Echo的消息
        System.out.printf("Client received: %s \n", o.toString());
    }
}