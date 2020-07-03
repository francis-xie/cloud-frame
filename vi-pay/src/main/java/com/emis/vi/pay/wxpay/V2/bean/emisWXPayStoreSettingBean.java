package com.emis.vi.pay.wxpay.V2.bean;

/**
 * $Id$
 * 需求 #40662 [标准版] 微信支付增加支持多收款账户-核心程式调整
 * 设置bean, 和作业Z042及table(PAY_SETTING_H) PS_TYPE = 'wxpay' 对应
 */
public class emisWXPayStoreSettingBean {
  private String PS_NO;
  private String PS_PRIMARY;
  private String WXPAY_MODE;
  private String WXPAY_APPID;
  private String WXPAY_MCHID;
  private String WXPAY_SUBAPPID;
  private String WXPAY_SUBMCHID;
  private String WXPAY_KEY;
  private String WXPAY_CERTLOCALPATH;
  private String WXPAY_CERTPASSWORD;

  public void emisWechatCustMPBean () {

  }

  public String getPS_NO() {
    return PS_NO;
  }

  public void setPS_NO(String PS_NO) {
    this.PS_NO = PS_NO;
  }

  public String getPS_PRIMARY() {
    return PS_PRIMARY;
  }

  public void setPS_PRIMARY(String PS_PRIMARY) {
    this.PS_PRIMARY = PS_PRIMARY;
  }

  public String getWXPAY_MODE() {
    return WXPAY_MODE;
  }

  public void setWXPAY_MODE(String WXPAY_MODE) {
    this.WXPAY_MODE = WXPAY_MODE;
  }

  public String getWXPAY_APPID() {
    return WXPAY_APPID;
  }

  public void setWXPAY_APPID(String WXPAY_APPID) {
    this.WXPAY_APPID = WXPAY_APPID;
  }

  public String getWXPAY_MCHID() {
    return WXPAY_MCHID;
  }

  public void setWXPAY_MCHID(String WXPAY_MCHID) {
    this.WXPAY_MCHID = WXPAY_MCHID;
  }

  public String getWXPAY_SUBAPPID() {
    return WXPAY_SUBAPPID;
  }

  public void setWXPAY_SUBAPPID(String WXPAY_SUBAPPID) {
    this.WXPAY_SUBAPPID = WXPAY_SUBAPPID;
  }

  public String getWXPAY_SUBMCHID() {
    return WXPAY_SUBMCHID;
  }

  public void setWXPAY_SUBMCHID(String WXPAY_SUBMCHID) {
    this.WXPAY_SUBMCHID = WXPAY_SUBMCHID;
  }

  public String getWXPAY_KEY() {
    return WXPAY_KEY;
  }

  public void setWXPAY_KEY(String WXPAY_KEY) {
    this.WXPAY_KEY = WXPAY_KEY;
  }

  public String getWXPAY_CERTLOCALPATH() {
    return WXPAY_CERTLOCALPATH;
  }

  public void setWXPAY_CERTLOCALPATH(String WXPAY_CERTLOCALPATH) {
    this.WXPAY_CERTLOCALPATH = WXPAY_CERTLOCALPATH;
  }

  public String getWXPAY_CERTPASSWORD() {
    return WXPAY_CERTPASSWORD;
  }

  public void setWXPAY_CERTPASSWORD(String WXPAY_CERTPASSWORD) {
    this.WXPAY_CERTPASSWORD = WXPAY_CERTPASSWORD;
  }

}