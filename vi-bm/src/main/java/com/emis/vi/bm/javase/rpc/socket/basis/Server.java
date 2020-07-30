package com.emis.vi.bm.javase.rpc.socket.basis;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * TCP编程：https://www.liaoxuefeng.com/wiki/1252599548343744/1305207629676577
 * 服务器端
 * 要使用Socket编程，我们首先要编写服务器端程序。Java标准库提供了ServerSocket来实现对指定IP和指定端口的监听。
 * ServerSocket的典型实现代码如下：
 */
public class Server {
    public static void main(String[] args) throws IOException {
        /* 服务器端通过代码：ServerSocket ss = new ServerSocket(6666);
           在指定端口6666监听。这里我们没有指定IP地址，表示在计算机的所有网络接口上进行监听。
        */
        ServerSocket ss = new ServerSocket(6666); // 监听指定端口
        System.out.println("server is running...");
        //如果ServerSocket监听成功，我们就使用一个无限循环来处理客户端的连接：
        for (; ; ) {
            /* 注意到代码ss.accept()表示每当有新的客户端连接进来后，就返回一个Socket实例，
               这个Socket实例就是用来和刚连接的客户端进行通信的。由于客户端很多，要实现并发处理，
               我们就必须为每个新的Socket创建一个新线程来处理，这样，主线程的作用就是接收新的连接，
               每当收到新连接后，就创建一个新线程进行处理。

               我们在多线程编程的章节中介绍过线程池，这里也完全可以利用线程池来处理客户端连接，能大大提高运行效率。

               如果没有客户端连接进来，accept()方法会阻塞并一直等待。如果有多个客户端同时连接进来，
               ServerSocket会把连接扔到队列里，然后一个一个处理。
               对于Java程序而言，只需要通过循环不断调用accept()就可以获取新的连接。
            */
            Socket sock = ss.accept();
            System.out.println("connected from " + sock.getRemoteSocketAddress());
            Thread t = new Handler(sock);
            t.start();
        }
    }
}

/**
 * 为每个新的Socket创建一个新线程来处理
 */
class Handler extends Thread {
    Socket sock;

    public Handler(Socket sock) {
        this.sock = sock;
    }

    @Override
    public void run() {
        /* Java的IO标准库提供的InputStream根据来源可以包括：
           FileInputStream：从文件读取数据，是最终数据源；
           ServletInputStream：从HTTP请求读取数据，是最终数据源；
           Socket.getInputStream()：从TCP连接读取数据，是最终数据源；
           ...
        */

        //InputStream就是Java标准库提供的最基本的输入流，它是所有输入流的超类。
        //InputStream是一个字节流，即以byte为单位读取。它位于java.io这个包里。java.io包提供了所有同步IO的功能。
        try (InputStream input = this.sock.getInputStream()) {
            /* 用try ... finally来编写关闭代码会感觉比较复杂，更好的写法是利用Java 7引入的新的try(resource)的语法，
               只需要编写try语句，让编译器自动为我们关闭资源。推荐的写法如下：
               实际上，编译器并不会特别地为InputStream加上自动关闭。
               编译器只看try(resource = ...)中的对象是否实现了java.lang.AutoCloseable接口，
               如果实现了，就自动加上finally语句并调用close()方法。InputStream和OutputStream都实现了这个接口，
               因此，都可以用在try(resource)中。*/

            //OutputStream是Java标准库提供的最基本的输出流，它是所有输出流的超类。
            try (OutputStream output = this.sock.getOutputStream()) {
                handle(input, output);
            }
        } catch (Exception e) {
            try {
                this.sock.close();
            } catch (IOException ioe) {
            }
            System.out.println("client disconnected.");
        }
    }

    private void handle(InputStream input, OutputStream output) throws IOException {
        //为了提高字符流读写的效率，引入了缓冲机制，进行字符批量的读写，提高了单个字符读写的效率。
        //BufferedReader用于加快读取字符的速度，BufferedWriter用于加快写入的速度

        //Writer就是带编码转换器的OutputStream，它把char转换为byte并输出。Writer是所有字符输出流的超类。
        //Writer是基于OutputStream构造的，可以通过OutputStreamWriter将OutputStream转换为Writer，转换时需要指定编码。
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        //Reader是Java的IO库提供的另一个输入流接口，它是一个字符流，即以char为单位读取
        //Reader定义了所有字符输入流的超类，Reader是带编码转换器的InputStream，它把byte转换为char
        //Reader是基于InputStream构造的：可以通过InputStreamReader在指定编码的同时将任何InputStream转换为Reader。
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        writer.write("hello\n");
        writer.flush();
        for (; ; ) {
            String s = reader.readLine();
            if (s.equals("bye")) {
                writer.write("bye\n");
                writer.flush();
                break;
            }
            writer.write("ok: " + s + "\n");
            writer.flush();
        }
    }
}