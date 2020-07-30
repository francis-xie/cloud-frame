package com.emis.vi.bm.javase.rpc.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.Charset;

/**
 * https://www.cnblogs.com/coding-diary/p/11503151.html
 * netty客户端
 */
public class EchoClient {

  public static void main(String[] args) {
    EventLoopGroup group = new NioEventLoopGroup();
    try {
      Bootstrap b = new Bootstrap(); //客户端的引导类，主要用于客户端连接远程主机
      b.group(group)
       .channel(NioSocketChannel.class)
       .option(ChannelOption.TCP_NODELAY, true)
       .handler(
        new ChannelInitializer<SocketChannel>() {
          @Override
          public void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline p = ch.pipeline();
            p.addLast(new EchoClientHandler());
          }
        });

      ChannelFuture f = b.connect("127.0.0.1", 8080).sync(); //连接TCP
      f.channel().closeFuture().sync();

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      group.shutdownGracefully();
    }
  }
}

class EchoClientHandler extends ChannelInboundHandlerAdapter {

  private final ByteBuf firstMessage;

  public EchoClientHandler() {
    firstMessage = Unpooled.buffer(256);
    for (int i = 0; i < firstMessage.capacity(); i++) {
      firstMessage.writeByte((byte) i);
    }
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) {
    ctx.writeAndFlush(firstMessage);
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    System.out.println("收到数据：" + ((ByteBuf) msg).toString(Charset.defaultCharset()));
    ctx.write(Unpooled.wrappedBuffer("Client message".getBytes()));
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {
    ctx.flush();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
    ctx.close();
  }
}