package com.emis.vi.bm.javase.rpc.thrift.demo1;

import org.apache.thrift.TException;

/**
 * 实现接口Iface
 */
public class HelloWorldImpl implements HelloWorldService.Iface {
  public HelloWorldImpl() {
  }

  @Override
  public String sayHello(String username) throws TException {
    return "Hi," + username + "Welcome to my blog http://www.cnblogs.com/zfygiser";
  }
}