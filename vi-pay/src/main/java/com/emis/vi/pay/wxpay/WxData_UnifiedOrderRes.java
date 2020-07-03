package com.emis.vi.pay.wxpay;

/**
 * 微信付款-统一下单: 返回结果
 * author: Harry
 * Date: 2016/06/23
 * Time: 下午2:30
 * 接口文档地址:
 * 商户： https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1
 * 服务商： https://pay.weixin.qq.com/wiki/doc/api/jsapi_sl.php?chapter=9_1
 * <p/>
 * 接口调用: https://api.mch.weixin.qq.com/pay/unifiedorder
 */
public class WxData_UnifiedOrderRes {

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
   * 如需在支付完成后获取sub_openid则此参数必传。
   */
  private String sub_appid;
  /**
   * 微信支付分配的子商户号
   */
  private String sub_mch_id;
  /**
   * 设备号
   */
  private String device_info;
  /**
   * 随机字符串
   */
  private String nonce_str;
  /**
   * 签名
   */
  private String sign;
  /**
   * 业务结果 (SUCCESS/FAIL)
   */
  private String result_code;
  /**
   * 错误代码
   */
  private String err_code;
  /**
   * 错误代码描述
   */
  private String err_code_des;

  // 以下字段在return_code 和result_code都为SUCCESS的时候有返回
  /**
   * 交易类型
   */
  private String trade_type;
  /**
   * 预支付交易会话标识
   * 微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时
   */
  private String prepay_id;
  /**
   * 二维码链接
   * trade_type为NATIVE是有返回，可将该参数值生成二维码展示出来进行扫码支付
   */
  private String code_url;

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

  public String getDevice_info() {
    return device_info;
  }

  public void setDevice_info(String device_info) {
    this.device_info = device_info;
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

  public String getResult_code() {
    return result_code;
  }

  public void setResult_code(String result_code) {
    this.result_code = result_code;
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

  public String getTrade_type() {
    return trade_type;
  }

  public void setTrade_type(String trade_type) {
    this.trade_type = trade_type;
  }

  public String getPrepay_id() {
    return prepay_id;
  }

  public void setPrepay_id(String prepay_id) {
    this.prepay_id = prepay_id;
  }

  public String getCode_url() {
    return code_url;
  }

  public void setCode_url(String code_url) {
    this.code_url = code_url;
  }

  public WxData_UnifiedOrderRes() {
  }

}