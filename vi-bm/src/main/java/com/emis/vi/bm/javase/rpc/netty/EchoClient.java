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
 * https://my.oschina.net/u/3728166/blog/2986455
 * netty客户端
 */
public class EchoClient {

  public static void main(String[] args) {
    //配置客户端NIO线程组
    EventLoopGroup group = new NioEventLoopGroup();
    try {
      /* Bootstrap：BootStrap 是客户端的引导类，主要用于客户端连接远程主机，有1个EventLoopGroup。
         Bootstrap 在调用 bind()（连接UDP）和 connect()（连接TCP）方法时，
         会新创建一个单独的、没有父 Channel 的 Channel 来实现所有的网络交换。*/

      Bootstrap b = new Bootstrap(); //客户端的引导类，主要用于客户端连接远程主机
      b.group(group) // 将NIO线程组传入客户端引导类中
       .channel(NioSocketChannel.class) //设置创建的Channel为NioSocketChannel类型
       .option(ChannelOption.TCP_NODELAY, true)
       .handler(new ChannelInitializer<SocketChannel>() { //设置绑定IO事件的处理类
         //创建NIOSocketChannel成功后，在进行初始化时，将它的ChannelHandler设置到ChannelPipeline中，用于处理网络IO事件
         @Override
         public void initChannel(SocketChannel ch) throws Exception {
           ChannelPipeline p = ch.pipeline(); // 获取处理器链
           p.addLast(new EchoClientHandler()); // 添加新的件处理器
         }
       });

      //发起异步连接操作
      ChannelFuture f = b.connect("127.0.0.1", 8080).sync(); //连接TCP
      f.channel().closeFuture().sync(); //等待客户端链路关闭
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      //优雅退出，释放NIO线程组
      group.shutdownGracefully();
    }
  }
}

/**
 * 客户端channel
 */
class EchoClientHandler extends ChannelInboundHandlerAdapter {

  private final ByteBuf firstMessage;

  public EchoClientHandler() {
    //byte[] req = "QUERY TIME ORDER".getBytes();
    //firstMessage = Unpooled.buffer(req.length);

    firstMessage = Unpooled.buffer(256);
    for (int i = 0; i < firstMessage.capacity(); i++) {
      firstMessage.writeByte((byte) i);
    }
  }

  //向服务器发送指令
  @Override
  public void channelActive(ChannelHandlerContext ctx) {
    ctx.writeAndFlush(firstMessage);
  }

  //接收服务器的响应
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    ByteBuf buf = (ByteBuf) msg;
    System.out.println("收到数据：" + buf.toString(Charset.defaultCharset()));
    //buf.readableBytes():获取缓冲区中可读的字节数；
    //根据可读字节数创建数组
    byte[] req = new byte[buf.readableBytes()];
    buf.readBytes(req);
    String body = new String(req, "UTF-8");
    System.out.println("Now is : " + body);

    ctx.write(Unpooled.wrappedBuffer("Client message".getBytes()));
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {
    ctx.flush();
  }

  //异常处理
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    //引发异常时关闭连接。
    cause.printStackTrace();
    //释放资源
    ctx.close();
  }
}