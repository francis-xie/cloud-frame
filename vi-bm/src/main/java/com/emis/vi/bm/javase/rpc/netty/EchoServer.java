package com.emis.vi.bm.javase.rpc.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.nio.charset.Charset;

/**
 * https://www.cnblogs.com/coding-diary/p/11503151.html
 * netty服务端
 */
public class EchoServer {

  public static void main(String[] args) {
    // accept线程组，用于服务端接受客户端的连接
    EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    // I/O线程组，用于进行SocketChannel的网络读写 处理业务逻辑
    EventLoopGroup workerGroup = new NioEventLoopGroup(1);

    try {
      /* ServerBootstrap： ServerBootstrap 是服务端的引导类，主要用户服务端绑定本地端口，
         有2个EventLoopGroup。ServerBootstarp 在调用 bind() 方法时会创建一个 ServerChannel 来接受来自客户端的连接，
         并且该 ServerChannel 管理了多个子 Channel 用于同客户端之间的通信。*/

      // Netty用于启动NIO服务器的辅助启动类
      ServerBootstrap b = new ServerBootstrap(); //服务端的引导类，主要用于服务端绑定本地端口
      b.group(bossGroup, workerGroup) // 将两个NIO线程组传入辅助启动类中
       .channel(NioServerSocketChannel.class) // 指定通道类型，设置创建的Channel为NioServerSocketChannel类型
       .option(ChannelOption.SO_BACKLOG, 100) // 设置TCP连接的缓冲区，配置NioServerSocketChannel的TCP参数
       .handler(new LoggingHandler(LogLevel.INFO)) // 设置日志级别
       .childHandler(new ChannelInitializer<SocketChannel>() { //设置绑定IO事件的处理类
         //创建NIOSocketChannel成功后，在进行初始化时，将它的ChannelHandler设置到ChannelPipeline中，用于处理网络IO事件
         @Override
         protected void initChannel(SocketChannel socketChannel) throws Exception {
           ChannelPipeline pipeline = socketChannel.pipeline(); // 获取处理器链
           pipeline.addLast(new EchoServerHandler()); // 添加新的件处理器
         }
       });

      // 通过bind启动服务，绑定端口，同步等待成功（sync()：同步阻塞方法，等待bind操作完成才继续）
      //ChannelFuture主要用于异步操作的通知回调
      ChannelFuture f = b.bind(8080).sync();
      System.out.println("服务端启动在8080端口。");
      // 阻塞主线程，直到网络服务被关闭/等待服务端监听端口关闭
      f.channel().closeFuture().sync();

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      //优雅退出，释放线程池资源
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
    }
  }
}

/**
 * 服务端channel
 */
class EchoServerHandler extends ChannelInboundHandlerAdapter {

  // 每当从客户端收到新的数据时，这个方法会在收到消息时被调用
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    ByteBuf buf = (ByteBuf) msg;
    System.out.println("收到数据：" + buf.toString(Charset.defaultCharset()));
    //buf.readableBytes():获取缓冲区中可读的字节数；
    //根据可读字节数创建数组
    byte[] req = new byte[buf.readableBytes()];
    buf.readBytes(req);
    String body = new String(req, "UTF-8");
    System.out.println("The server(Thread:" + Thread.currentThread() + ") receive order : " + body);

    ByteBuf resp = Unpooled.wrappedBuffer("Server message".getBytes());
    //将待发送的消息放到发送缓存数组中
    ctx.write(resp); //ctx.writeAndFlush(resp);
    ctx.fireChannelRead(msg);
  }

  // 数据读取完后被调用
  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    ctx.flush();
  }

  // 当Netty由于IO错误或者处理器在处理事件时抛出的异常时被调用
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.close();
  }
}