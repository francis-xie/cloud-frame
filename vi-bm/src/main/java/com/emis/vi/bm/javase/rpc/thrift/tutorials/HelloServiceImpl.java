package com.emis.vi.bm.javase.rpc.thrift.tutorials;

import org.apache.thrift.TException;

/**
 * 创建 HelloServiceImpl.java 文件并实现 Hello.java 文件中的 Hello.Iface 接口，代码如下：
 * HelloServiceImpl.java是用户实现的业务逻辑
 * Hello.java是根据 Thrift 定义的服务接口描述文件(Hello.thrift)生成的客户端和服务器端代码框架，
 * 其中有根据 Thrift 文件生成代码实现数据的读写操作。
 */
public class HelloServiceImpl implements Hello.Iface {
  @Override
  public boolean helloBoolean(boolean para) throws TException {
    return para;
  }

  @Override
  public int helloInt(int para) throws TException {
    try {
      Thread.sleep(20000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return para;
  }

  @Override
  public String helloNull() throws TException {
    return null;
  }

  @Override
  public String helloString(String para) throws TException {
    return para;
  }

  @Override
  public void helloVoid() throws TException {
    System.out.println("Hello World");
  }
}