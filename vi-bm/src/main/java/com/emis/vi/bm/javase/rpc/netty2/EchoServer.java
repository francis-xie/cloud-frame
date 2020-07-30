package com.emis.vi.bm.javase.rpc.netty2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        final EchoServerInboundHandler serverHandler = new EchoServerInboundHandler();
        EventLoopGroup group = new NioEventLoopGroup(); // 传输类型使用NIO
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group) // 配置EventLoopGroup
                    .channel(NioServerSocketChannel.class) // 配置Channel的类型
                    .localAddress(new InetSocketAddress(port)) // 配置端口号
                    .childHandler(new ChannelInitializer() {
                        // 实现一个ChannelInitializer，它可以方便地添加多个ChannelHandler
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline().addLast(serverHandler);
                        }

                        // 实现一个ChannelInitializer，它可以方便地添加多个ChannelHandler
                        /*@Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(serverHandler);
                        }*/
                    });
            // 绑定地址，同步等待它完成
            ChannelFuture f = b.bind().sync();
            // 关闭这个Future
            f.channel().closeFuture().sync();
        } finally {
            // 关闭应用程序，一般来说Netty应用只需要调用这个方法就够了
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.printf(
                    "Usage: %s  \n",
                    EchoServer.class.getSimpleName()
            );
            return;
        }
        int port = Integer.parseInt(args[0]);
        new EchoServer(port).start();
    }
}