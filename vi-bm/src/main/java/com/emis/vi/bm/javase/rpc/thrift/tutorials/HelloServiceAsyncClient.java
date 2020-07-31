package com.emis.vi.bm.javase.rpc.thrift.tutorials;

import java.io.IOException;

import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TNonblockingSocket;
import org.apache.thrift.transport.TNonblockingTransport;

/**
 * 创建异步客户端实现代码，调用 Hello.AsyncClient 访问服务端的逻辑实现，
 * 将 MethodCallback 对象作为参数传入调用方法中，代码如下：
 * HelloServiceAsyncClient 通过 java.nio.channels.Socketchannel 创建异步客户端与服务器建立连接。
 * 在本文中异步客户端通过以下的循环代码实现了同步效果，读者可去除这部分代码后再运行对比。
 */
public class HelloServiceAsyncClient {

  /**
   * 调用 Hello 服务
   *
   * @param args
   */
  public static void main(String[] args) throws Exception {
    try {
      TAsyncClientManager clientManager = new TAsyncClientManager();
      TNonblockingTransport transport = new TNonblockingSocket(
       "localhost", 10005);
      TProtocolFactory protocol = new TBinaryProtocol.Factory();
      Hello.AsyncClient asyncClient = new Hello.AsyncClient(protocol,
       clientManager, transport);
      System.out.println("Client calls .....");
      MethodCallback callBack = new MethodCallback();
      asyncClient.helloString("Hello World", callBack);
      //异步客户端实现同步效果代码段
      Object res = callBack.getResult();
      // 等待服务调用后的返回结果
      while (res == null) {
        res = callBack.getResult();
      }
      System.out.println(((Hello.AsyncClient.helloString_call) res)
       .getResult());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}