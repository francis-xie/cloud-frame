package com.emis.vi.pay.alipay.util.httpClient;

import com.emis.vi.pay.alipay.config.AlipayConfig;

import org.apache.commons.httpclient.Header;

import java.io.UnsupportedEncodingException;

/*$Id: HttpResponse.java 14052 2014-12-16 10:33:05Z francis.xie $ *
 *类名：HttpResponse
 *功能：Http返回对象的封装
 *详细：封装Http返回信息
 *2014/12/06 Francis.xie Modify
 */

public class HttpResponse {

  /**
   * 返回中的Header信息
   */
  private Header[] responseHeaders;

  /**
   * String类型的result
   */
  private String stringResult;

  /**
   * btye类型的result
   */
  private byte[] byteResult;

  public Header[] getResponseHeaders() {
    return responseHeaders;
  }

  public void setResponseHeaders(Header[] responseHeaders) {
    this.responseHeaders = responseHeaders;
  }

  public byte[] getByteResult() {
    if (byteResult != null) {
      return byteResult;
    }
    if (stringResult != null) {
      return stringResult.getBytes();
    }
    return null;
  }

  public void setByteResult(byte[] byteResult) {
    this.byteResult = byteResult;
  }

  public String getStringResult() throws UnsupportedEncodingException {
    if (stringResult != null) {
      return stringResult;
    }
    if (byteResult != null) {
      return new String(byteResult, AlipayConfig.input_charset);
    }
    return null;
  }

  public void setStringResult(String stringResult) {
    this.stringResult = stringResult;
  }

}
