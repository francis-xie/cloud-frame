/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.emis.vi.pay.alipay.constants;

import org.apache.log4j.Logger;

/**
 * $Id: AlipayServiceEnvConstants.java 14854 2015-06-18 10:27:12Z francis.xie $
 * 2015/06/17 Francis.xie Modify 需求 #30229 [标准版-Venus] 支付宝升级1.0升到2.0
 * 2018/01/19 Francis.xie Modify 需求 #42037 [标准版-元朗]支持新版支付宝 支持参数设定RSA签名
 * 支付宝服务窗环境常量（demo中常量只是参考，需要修改成自己的常量值）
 *
 * @author taixu.zqq
 * @version $Id: AlipayServiceEnvConstants.java 14854 2015-06-18 10:27:12Z francis.xie $
 */
public class AlipayServiceEnvConstants {
  /**
   * Log日志档
   */
  public static Logger logger = null;

  /**
   * 支付宝公钥-从支付宝服务窗获取
   */
  public static String ALIPAY_PUBLIC_KEY = "";

  /**
   * 字符编码-传递给支付宝的数据编码
   */
  public static final String CHARSET = "UTF-8";

  /**
   * 服务窗appId
   */
  public static String APP_ID = "";

  //开发者请使用openssl生成的密钥替换此处  请看文档：https://fuwu.alipay.com/platform/doc.htm#2-1接入指南
  public static String PRIVATE_KEY = "";

  /**
   * 支付宝网关
   */
  public static String ALIPAY_GATEWAY = "";

  /**
   * 商品编号
   */
  public static String goods_id = "";

  /**
   * 商品名称
   */
  public static String goods_name = "";

  /**
   * 系统商编号 6.如果是支付宝有返佣协议的合作商户，
   * 需要传入 extend_params 参数，并且在参数中传入 sys_service _provider_id，值为系统商签约账户的 partnerId。
   */
  public static String sys_service_provider_id = "";

  /**
   * 签名算法RSA/RSA2
   * 目前支付宝商户后台已不支持RSA的密钥设定，只能设定RSA2，新上客户需要改为RSA2签名，默认还是RSA旧客户可以使用
   * RSA:SHA1WithRSA对RSA密钥的长度不限制，推荐使用2048位以上
   * RSA2:SHA256WithRSA,（强烈推荐使用），强制要求RSA密钥的长度至少为2048
   */
  public static String ALIPAY_SIGN_TYPE = "RSA";
}