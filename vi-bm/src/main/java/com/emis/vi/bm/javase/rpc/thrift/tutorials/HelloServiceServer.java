package com.emis.vi.bm.javase.rpc.thrift.tutorials;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TBinaryProtocol.Factory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

/**
 * https://developer.ibm.com/zh/tutorials/j-lo-apachethrift/
 * Thrift 架构：Thrift 包含一个完整的堆栈结构用于构建客户端和服务器端。
 * Thrift 的传输体系、协议以及底层 I/O 通信，
 * 使用 Thrift 可以很方便的定义一个服务并且选择不同的传输协议和传输层而不用重新生成代码。
 * <p>
 * Thrift 服务器包含用于绑定协议和传输层的基础架构，它提供阻塞、非阻塞、单线程和多线程的模式运行在服务器上，
 * 可以配合服务器 / 容器一起运行，可以和现有的 J2EE 服务器 /Web 容器无缝的结合。
 * <p>
 * 创建服务器端实现代码，将 HelloServiceImpl 作为具体的处理器传递给 Thrift 服务器，代码如下：
 * <p>
 * 运行服务器，再启动客户端调用服务 Hello 的方法 helloVoid，
 * 在服务器端的控制台窗口输出 “Hello World”(helloVoid 方法实现在控制台打印字符串，没有返回值，
 * 所以客户端调用方法后没有返回值输出 )。
 */
public class HelloServiceServer {

  /**
   * 启动 Thrift 服务器
   *
   * @param args
   */
  public static void main(String[] args) {
    startServer();
  }

  /**
   * Server 端启动、服务时序
   * HelloServiceServer 启动的过程以及服务被客户端调用时，服务器的响应过程。
   * 程序调用了 TThreadPoolServer 的 serve 方法后，server 进入阻塞监听状态，
   * 其阻塞在 TServerSocket 的 accept 方法上。
   * 当接收到来自客户端的消息后，服务器发起一个新线程处理这个消息请求，原线程再次进入阻塞状态。
   * 在新线程中，服务器通过 TBinaryProtocol 协议读取消息内容，调用 HelloServiceImpl 的 helloVoid 方法，
   * 并将结果写入 helloVoid_result 中传回客户端。
   */
  public static void startServer() {
    try {
      // 设置服务端口为 7911
      TServerSocket serverTransport = new TServerSocket(7911);
      TThreadPoolServer.Args args = new TThreadPoolServer.Args(serverTransport);
      // 设置协议工厂为 TBinaryProtocol.Factory
      Factory proFactory = new TBinaryProtocol.Factory(); //二进制编码格式进行数据传输
      // 关联处理器与 Hello 服务的实现
      TProcessor processor = new Hello.Processor(new HelloServiceImpl());
      args.processor(processor);
      args.protocolFactory(proFactory);
      TServer server = new TThreadPoolServer(args); //多线程服务器端使用标准的阻塞式 I/O
      //TSimpleServer —— 单线程服务器端使用标准的阻塞式 I/O 代码如下：
      //TServer server = new TSimpleServer(args);
      System.out.println("Start server on port 7911...");
      server.serve();
    } catch (TTransportException e) {
      e.printStackTrace();
    }
  }
}