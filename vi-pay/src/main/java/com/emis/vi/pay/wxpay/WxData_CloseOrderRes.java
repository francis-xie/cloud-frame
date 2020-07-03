package com.emis.vi.pay.wxpay;

/**
 * 微信付款-关闭订单: 返回结果
 * author: Harry
 * Date: 2016/06/27
 * Time: 下午4:29
 * 接口文档地址:
 * 商户： https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_3
 * 服务商： https://pay.weixin.qq.com/wiki/doc/api/jsapi_sl.php?chapter=9_3
 * <p/>
 * 接口调用: https://api.mch.weixin.qq.com/pay/closeorder
 */
public class WxData_CloseOrderRes {

  /**
   * 返回状态码 (SUCCESS/FAIL)
   */
  private String return_code;
  /**
   * 返回信息
   */
  private String return_msg;

  // 以下字段在return_code为SUCCESS的时候有返回
  /**
   * 公众账号ID
   * 调用接口提交的公众账号ID
   */
  private String appid;
  /**
   * 商户号
   */
  private String mch_id;
  /**
   * 微信分配的子商户公众账号ID
   */
  private String sub_appid;
  /**
   * 微信支付分配的子商户号
   */
  private String sub_mch_id;
  /**
   * 随机字符串
   */
  private String nonce_str;
  /**
   * 签名
   */
  private String sign;
  /**
   * 错误代码
   */
  private String err_code;
  /**
   * 错误代码描述
   */
  private String err_code_des;


  public String getReturn_code() {
    return return_code;
  }

  public void setReturn_code(String return_code) {
    this.return_code = return_code;
  }

  public String getReturn_msg() {
    return return_msg;
  }

  public void setReturn_msg(String return_msg) {
    this.return_msg = return_msg;
  }

  public String getAppid() {
    return appid;
  }

  public void setAppid(String appid) {
    this.appid = appid;
  }

  public String getMch_id() {
    return mch_id;
  }

  public void setMch_id(String mch_id) {
    this.mch_id = mch_id;
  }

  public String getSub_appid() {
    return sub_appid;
  }

  public void setSub_appid(String sub_appid) {
    this.sub_appid = sub_appid;
  }

  public String getSub_mch_id() {
    return sub_mch_id;
  }

  public void setSub_mch_id(String sub_mch_id) {
    this.sub_mch_id = sub_mch_id;
  }

  public String getNonce_str() {
    return nonce_str;
  }

  public void setNonce_str(String nonce_str) {
    this.nonce_str = nonce_str;
  }

  public String getSign() {
    return sign;
  }

  public void setSign(String sign) {
    this.sign = sign;
  }

  public String getErr_code() {
    return err_code;
  }

  public void setErr_code(String err_code) {
    this.err_code = err_code;
  }

  public String getErr_code_des() {
    return err_code_des;
  }

  public void setErr_code_des(String err_code_des) {
    this.err_code_des = err_code_des;
  }

  public WxData_CloseOrderRes() {
  }

}