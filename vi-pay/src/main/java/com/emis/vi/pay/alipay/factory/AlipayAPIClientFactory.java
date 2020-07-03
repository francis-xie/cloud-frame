/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.emis.vi.pay.alipay.factory;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.emis.vi.pay.alipay.constants.AlipayServiceEnvConstants;


/**
 * $Id: AlipayAPIClientFactory.java 14854 2015-06-18 10:27:12Z francis.xie $
 * 2015/06/17 Francis.xie Modify 需求 #30229 [标准版-Venus] 支付宝升级1.0升到2.0
 * 2018/01/19 Francis.xie Modify 需求 #42037 [标准版-元朗]支持新版支付宝 支持参数设定RSA签名
 * API调用客户端工厂
 *
 * @version $Id: AlipayAPIClientFactory.java 14854 2015-06-18 10:27:12Z francis.xie $
 */
public class AlipayAPIClientFactory {

  /**
   * API调用客户端
   */
  private static AlipayClient alipayClient;

  /**
   * 获得API调用客户端
   *
   * @return
   */
  public static AlipayClient getAlipayClient() {
    if (null == alipayClient) {
      alipayClient = new DefaultAlipayClient(AlipayServiceEnvConstants.ALIPAY_GATEWAY, AlipayServiceEnvConstants.APP_ID,
        AlipayServiceEnvConstants.PRIVATE_KEY, "json", AlipayServiceEnvConstants.CHARSET, AlipayServiceEnvConstants.ALIPAY_PUBLIC_KEY,
        AlipayServiceEnvConstants.ALIPAY_SIGN_TYPE);
    }
    return alipayClient;
  }
}
