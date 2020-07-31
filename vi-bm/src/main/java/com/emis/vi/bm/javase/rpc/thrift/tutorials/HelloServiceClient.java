package com.emis.vi.bm.javase.rpc.thrift.tutorials;

import org.apache.thrift.TApplicationException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

/**
 * 创建客户端实现代码，调用 Hello.client 访问服务端的逻辑实现，代码如下：
 */
public class HelloServiceClient {

  /**
   * 调用 Hello 服务
   *
   * @param args
   */
  public static void main(String[] args) {
    startClient();
  }

  /**
   * Client 端调用服务时序
   * HelloServiceClient 调用服务的过程以及接收到服务器端的返回值后处理结果的过程。
   * 程序调用了 Hello.Client 的 helloVoid 方法，在 helloVoid 方法中，
   * 通过 send_helloVoid 方法发送对服务的调用请求，通过 recv_helloVoid 方法接收服务处理请求后返回的结果。
   */
  public static void startClient() {
    try {
      // 设置调用的服务地址为本地，端口为 7911
      TTransport transport = new TSocket("localhost", 7911); //使用阻塞式 I/O 进行传输
      transport.open();
      // 设置传输协议为 TBinaryProtocol
      TProtocol protocol = new TBinaryProtocol(transport); //二进制编码格式进行数据传输
      Hello.Client client = new Hello.Client(protocol);
      // 调用服务的 helloVoid 方法
      client.helloVoid();
      System.out.println("Hello " + client.helloString("World"));
      //调用 helloNull 方法后，会抛出 TApplicationException 异常，并且异常种类为 MISSING_RESULT，
      //本段代码显示，捕获该异常后，直接在控制台打印“The result of helloNull function is NULL”信息。
      System.out.println(client.helloNull()); //调用 Hello 服务，并处理 null 值问题
      transport.close();
    } catch (TTransportException e) {
      e.printStackTrace();
    } catch (TException e) {
      //处理 null 值问题
      if (e instanceof TApplicationException
       && ((TApplicationException) e).getType() == TApplicationException.MISSING_RESULT) {
        System.out.println("The result of helloNull function is NULL");
      }
    }
  }
}