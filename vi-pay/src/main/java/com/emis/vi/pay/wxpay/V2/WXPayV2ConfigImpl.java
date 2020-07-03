package com.emis.vi.pay.wxpay.V2;

import com.emis.vi.pay.wxpay.V2.common.IWXPayDomain;
import com.emis.vi.pay.wxpay.V2.common.WXPayConfig;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class WXPayV2ConfigImpl extends WXPayConfig {

  private byte[] certData;

  private String appID;
  private String mchID;
  private String key;
  private String certPath;

//  private static WxPayV2ConfigImpl INSTANCE;
//  public static WxPayV2ConfigImpl getInstance() throws Exception{
//    if (INSTANCE == null) {
//      synchronized (WxPayV2ConfigImpl.class) {
//        if (INSTANCE == null) {
//          INSTANCE = new WxPayV2ConfigImpl();
//        }
//      }
//    }
//    return INSTANCE;
//  }

  public WXPayV2ConfigImpl(String appID, String mchID, String key, String certPath) throws Exception {
    this.appID = appID;
    this.mchID = mchID;
    this.key = key;
    this.certPath = certPath;
    init();
  }

  public WXPayV2ConfigImpl() throws Exception {

  }

  public void init() throws Exception {
//    String certPath = "D://CERT/common/apiclient_cert.p12";
    File file = new File(certPath);
    InputStream certStream = new FileInputStream(file);
    this.certData = new byte[(int) file.length()];
    certStream.read(this.certData);
    certStream.close();
  }

  public String getAppID() {
    return appID;
  }

  public void setAppID(String appID) {
    this.appID = appID;
  }

  public String getMchID() {
    return mchID;
  }

  public void setMchID(String mchID) {
    this.mchID = mchID;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getCertPath() {
    return certPath;
  }

  public void setCertPath(String certPath) {
    this.certPath = certPath;
  }

  public InputStream getCertStream() {
    ByteArrayInputStream certBis;
    certBis = new ByteArrayInputStream(this.certData);
    return certBis;
  }

  public int getHttpConnectTimeoutMs() {
    return 2000;
  }

  public int getHttpReadTimeoutMs() {
    return 10000;
  }

  public IWXPayDomain getWXPayDomain() {
//    return WXPayDomainSimpleImpl.instance();
    return new WXPayV2DomainSimpleImpl();
  }

  public String getPrimaryDomain() {
    return "api.mch.weixin.qq.com";
  }

  public String getAlternateDomain() {
    return "api2.mch.weixin.qq.com";
  }

  @Override
  public int getReportWorkerNum() {
    return 1;
  }

  @Override
  public int getReportBatchSize() {
    return 2;
  }

}