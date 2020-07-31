package com.emis.vi.bm.javase.rpc.thrift.tutorials;

import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TTransportException;

/**
 * 创建非阻塞服务器端实现代码，将 HelloServiceImpl 作为具体的处理器传递给异步 Thrift 服务器，代码如下：
 * HelloServiceAsyncServer 通过 java.nio.channels.ServerSocketChannel 创建非阻塞的服务器端等待客户端的连接。
 */
public class HelloServiceAsyncServer {
  /**
   * 启动 Thrift 异步服务器
   *
   * @param args
   */
  public static void main(String[] args) {
    startServer();
  }

  public static void startServer() {
    TNonblockingServerTransport serverTransport;
    try {
      serverTransport = new TNonblockingServerSocket(10005);
      TNonblockingServer.Args args = new TNonblockingServer.Args(serverTransport);
      Hello.Processor processor = new Hello.Processor(new HelloServiceImpl());
      args.processor(processor);
      //args.protocolFactory(proFactory);
      TServer server = new TNonblockingServer(args);
      System.out.println("Start server on port 10005 ...");
      server.serve();
    } catch (TTransportException e) {
      e.printStackTrace();
    }
  }
}