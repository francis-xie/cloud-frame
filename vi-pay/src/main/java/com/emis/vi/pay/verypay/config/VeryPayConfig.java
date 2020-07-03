package com.emis.vi.pay.verypay.config;

import org.apache.log4j.Logger;

/**
 * $Id$
 * 支付相关配置
 * 2018/02/02 Francis.xie Modify 需求 #42028 [客制-宜芝多]费睿
 */
public class VeryPayConfig {
  private static Logger log = null;

  /*private static Properties properties = new Properties();
  static {
    InputStream in = null;
    try {
      String fileName = "config.properties";
      String filePath = new File(fileName).getAbsolutePath();
      filePath = filePath.replace("bin\\config.properties", "config.properties");

      in = new FileInputStream(filePath);
      properties.load(in);

      // 加载配置文件,初始化内容
      setAppKey(properties.getProperty("appKey"));
      setAppSecret(properties.getProperty("appSecret"));
      setStoreSn(properties.getProperty("storeSn"));
      setClientSn(properties.getProperty("clientSn"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (null != in)
          in.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  *//**
   * 沙箱地址
   *//*
  private static String API_URL_SANDBOX = "https://api.pay.verystar.cn/sandbox/";

  *//**
   * 正式地址
   *//*
  private static String API_URL_RELEASE = "https://api.pay.verystar.cn/v3/";

  *//**
   * 连接超时时间
   *//*
  private static int connectTimeout = 30;

  *//**
   * 读取超时时间
   *//*
  private static int readTimeout = 30;

  *//**
   * 写入超时时间
   *//*
  private static int writeTimeout = 30;*/

  /**
   * 设置debug
   */
  private static boolean isDebug;

  /**
   * VeryStar轻易付平台网络连线地址
   */
  private static String apiUrl;

  /**
   * API分配的公钥,和私钥是一对,私钥用于请求加密
   */
  private static String appKey;

  /**
   * 私钥
   */
  private static String appSecret;

  // 验证数据完整性
  public static boolean verify() {
    if (apiUrl == null) {
      log.error("apiUrl不能为空");
      return false;
    }
    if (appKey == null) {
      log.error("appKey不能为空");
      return false;
    }
    if (appSecret == null) {
      log.error("appSecret不能为空");
      return false;
    }
    return true;
  }

  public static Logger getLog() {
    return VeryPayConfig.log;
  }

  public static void setLog(Logger log) {
    VeryPayConfig.log = log;
  }

  public static String getAppSecret() {
    return VeryPayConfig.appSecret;
  }

  public static void setAppSecret(String appSecret) {
    VeryPayConfig.appSecret = appSecret;
  }

  public static String getAppKey() {
    return VeryPayConfig.appKey;
  }

  public static void setAppKey(String appKey) {
    VeryPayConfig.appKey = appKey;
  }

  public static String getApiUrl() {
    return VeryPayConfig.apiUrl;
  }

  public static void setApiUrl(String apiUrl) {
    VeryPayConfig.apiUrl = apiUrl;
  }

  public static boolean isDebug() {
    return VeryPayConfig.isDebug;
  }

  public static void setIsDebug(boolean isDebug) {
    VeryPayConfig.isDebug = isDebug;
  }
}