package com.emis.vi.bm.javase.rpc.thrift.tutorials;

import org.apache.thrift.async.AsyncMethodCallback;

/**
 * CallBack 的实现：MethodCallback.java
 * onComplete 方法接收服务处理后的结果，此处我们将结果 response 直接赋值给 callback 的私有属性 response。
 * onError 方法接收服务处理过程中抛出的异常，此处未对异常进行处理。
 */
public class MethodCallback implements AsyncMethodCallback {
  Object response = null;

  public Object getResult() {
    // 返回结果值
    return this.response;
  }

  // 处理服务返回的结果值
  @Override
  public void onComplete(Object response) {
    this.response = response;
  }

  // 处理调用服务过程中出现的异常
  @Override
  public void onError(Exception e) {

  }
}