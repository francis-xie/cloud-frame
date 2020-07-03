package com.emis.vi.pay.alipay.config;

/* $Id: AlipayConfig.java 15974 2016-04-27 01:32:43Z kiro.tang $*
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *2014/12/06 Francis.xie Modify
	
 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */

import org.apache.log4j.Logger;

public class AlipayConfig {

  /**
   * Log 日志档
   */
  public static Logger logger = null;

  //↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
  // 合作身份者ID，以2088开头由16位纯数字组成的字符串
  public static String partner = "";
  // 商户的私钥
  public static String key = "";
  // 支付宝提供给商户的服务接入网关URL(新) https://mapi.alipay.com/gateway.do?
  public static String url = "";

  //↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑


  // 调试用，创建TXT日志文件夹路径
  public static String log_path = "D:\\";

  // 字符编码格式 目前支持 gbk 或 utf-8
  public static String input_charset = "utf-8";

  // 签名方式 不需修改
  public static String sign_type = "MD5";

}
