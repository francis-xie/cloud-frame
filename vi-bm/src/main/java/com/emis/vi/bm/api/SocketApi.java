package com.emis.vi.bm.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.*;

/**
 * 传统的 Socket 实现
 */
public class SocketApi {
    public static void main(String[] args) {
        //testIoSokcet();
        //testNioSokcet();
        testAioSokcet();
    }

    public static void testIoSokcet() {
        //实现一个简单的 Socket，服务器端只发给客户端信息，再由客户端打印出来的例子
        //利用 Socket 模拟了一个简单的客户端，只进行连接、读取和打印
        int port = 4343; //端口号
        // Socket 服务器端（简单的发送信息）
        Thread sThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(port);
                    while (true) {
                        // 等待连接
                        Socket socket = serverSocket.accept(); //调用 accept 方法，阻塞等待客户端连接
                        Thread sHandlerThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try (PrintWriter printWriter = new PrintWriter(socket.getOutputStream())) {
                                    printWriter.println("hello world！");
                                    printWriter.flush();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        sHandlerThread.start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        sThread.start();

        // Socket 客户端（接收信息并打印）
        try (Socket cSocket = new Socket(InetAddress.getLocalHost(), port)) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
            bufferedReader.lines().forEach(s -> System.out.println("客户端：" + s));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void testNioSokcet() {
        //NIO 多路复用
        //介于高并发的问题，NIO 的多路复用功能就显得意义非凡了。
        //NIO 是利用了单线程轮询事件的机制，通过高效地定位就绪的 Channel，来决定做什么，
        //仅仅 select 阶段是阻塞的，可以有效避免大量客户端连接时，频繁线程切换带来的问题，应用的扩展能力有了非常大的提高。

        int port = 4343; //端口号
        // NIO 多路复用
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(4, 4,
                60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try (Selector selector = Selector.open(); //首先，通过 Selector.open() 创建一个 Selector，作为类似调度员的角色
                     //然后，创建一个 ServerSocketChannel，并且向 Selector 注册，通过指定 SelectionKey.OP_ACCEPT，告诉调度员，它关注的是新的连接请求
                     ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();) {
                    serverSocketChannel.bind(new InetSocketAddress(InetAddress.getLocalHost(), port));
                    serverSocketChannel.configureBlocking(false);
                    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
                    while (true) {
                        //Selector 阻塞在 select 操作，当有 Channel 发生接入请求，就会被唤醒
                        selector.select(); // 阻塞等待就绪的Channel
                        Set<SelectionKey> selectionKeys = selector.selectedKeys();
                        Iterator<SelectionKey> iterator = selectionKeys.iterator();
                        while (iterator.hasNext()) {
                            SelectionKey key = iterator.next();
                            try (SocketChannel channel = ((ServerSocketChannel) key.channel()).accept()) {
                                channel.write(Charset.defaultCharset().encode("你好，世界"));
                            }
                            iterator.remove();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // Socket 客户端（接收信息并打印）
        try (Socket cSocket = new Socket(InetAddress.getLocalHost(), port)) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
            bufferedReader.lines().forEach(s -> System.out.println("NIO 客户端：" + s));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void testAioSokcet() {
        try {
            //AIO 版 Socket 实现，Java 1.7 提供了 AIO 实现的 Socket
            int port = 4343; //端口号
            // AIO线程复用版
            Thread sThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    AsynchronousChannelGroup group = null;
                    try {
                        group = AsynchronousChannelGroup.withThreadPool(Executors.newFixedThreadPool(4));
                        AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open(group).bind(new InetSocketAddress(InetAddress.getLocalHost(), port));
                        server.accept(null, new CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel>() {
                            @Override
                            public void completed(AsynchronousSocketChannel result, AsynchronousServerSocketChannel attachment) {
                                server.accept(null, this); // 接收下一个请求
                                try {
                                    Future<Integer> f = result.write(Charset.defaultCharset().encode("你好，世界"));
                                    f.get();
                                    System.out.println("服务端发送时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                    result.close();
                                } catch (InterruptedException | ExecutionException | IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {
                            }
                        });
                        group.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            sThread.start();

            // Socket 客户端
            AsynchronousSocketChannel client = AsynchronousSocketChannel.open();
            Future<Void> future = client.connect(new InetSocketAddress(InetAddress.getLocalHost(), port));
            future.get();
            ByteBuffer buffer = ByteBuffer.allocate(100);
            client.read(buffer, null, new CompletionHandler<Integer, Void>() {
                @Override
                public void completed(Integer result, Void attachment) {
                    System.out.println("客户端打印：" + new String(buffer.array()));
                }

                @Override
                public void failed(Throwable exc, Void attachment) {
                    exc.printStackTrace();
                    try {
                        client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            Thread.sleep(10 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
