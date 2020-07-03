package com.emis.vi.pay.wxpay;

/**
 * 微信付款-申请退款: 返回结果
 * author: Harry
 * Date: 2016/06/27
 * Time: 下午4:29
 * 接口文档地址:
 * 商户： https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_4
 * 服务商： https://pay.weixin.qq.com/wiki/doc/api/jsapi_sl.php?chapter=9_4
 * <p/>
 * 接口调用: https://api.mch.weixin.qq.com/secapi/pay/refund
 */
public class WxData_RefundRes {

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
   * 业务结果 (SUCCESS/FAIL)
   * SUCCESS退款申请接收成功，结果通过退款查询接口查询; FAIL 提交业务失败
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
   * 微信订单号
   */
  private String transaction_id;
  /**
   * 商户订单号
   */
  private String out_trade_no;
  /**
   * 商户退款单号
   */
  private String out_refund_no;
  /**
   * 微信退款单号
   */
  private String refund_id;
  /**
   * 退款渠道
   * ORIGINAL—原路退款; BALANCE—退回到余额
   */
  private String refund_channel;
  /**
   * 申请退款金额
   */
  private int refund_fee;
  /**
   * 退款金额
   */
  private int settlement_refund_fee_$n;
  /**
   * 订单金额
   */
  private int total_fee;
  /**
   * 应结订单金额
   */
  private int settlement_total_fee;
  /**
   * 订单金额货币种类
   */
  private String fee_type;
  /**
   * 现金支付金额
   */
  private int cash_fee;
  /**
   * 现金退款金额
   */
  private int cash_refund_fee;
  /**
   * 代金券类型
   */
  private String coupon_type_$n;
  /**
   * 代金券退款金额
   */
  private int coupon_refund_fee_$n;
  /**
   * 退款代金券使用数量
   */
  private int coupon_refund_count_$n;
  /**
   * 退款代金券批次ID
   */
  private String coupon_refund_batch_id_$n_$m;
  /**
   * 退款代金券ID
   */
  private String coupon_refund_id_$n_$m;
  /**
   * 单个退款代金券支付金额
   */
  private int coupon_refund_fee_$n_$m;

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

  public String getTransaction_id() {
    return transaction_id;
  }

  public void setTransaction_id(String transaction_id) {
    this.transaction_id = transaction_id;
  }

  public String getOut_trade_no() {
    return out_trade_no;
  }

  public void setOut_trade_no(String out_trade_no) {
    this.out_trade_no = out_trade_no;
  }

  public String getOut_refund_no() {
    return out_refund_no;
  }

  public void setOut_refund_no(String out_refund_no) {
    this.out_refund_no = out_refund_no;
  }

  public String getRefund_id() {
    return refund_id;
  }

  public void setRefund_id(String refund_id) {
    this.refund_id = refund_id;
  }

  public String getRefund_channel() {
    return refund_channel;
  }

  public void setRefund_channel(String refund_channel) {
    this.refund_channel = refund_channel;
  }

  public int getRefund_fee() {
    return refund_fee;
  }

  public void setRefund_fee(int refund_fee) {
    this.refund_fee = refund_fee;
  }

  public int getSettlement_refund_fee_$n() {
    return settlement_refund_fee_$n;
  }

  public void setSettlement_refund_fee_$n(int settlement_refund_fee_$n) {
    this.settlement_refund_fee_$n = settlement_refund_fee_$n;
  }

  public int getTotal_fee() {
    return total_fee;
  }

  public void setTotal_fee(int total_fee) {
    this.total_fee = total_fee;
  }

  public int getSettlement_total_fee() {
    return settlement_total_fee;
  }

  public void setSettlement_total_fee(int settlement_total_fee) {
    this.settlement_total_fee = settlement_total_fee;
  }

  public String getFee_type() {
    return fee_type;
  }

  public void setFee_type(String fee_type) {
    this.fee_type = fee_type;
  }

  public int getCash_fee() {
    return cash_fee;
  }

  public void setCash_fee(int cash_fee) {
    this.cash_fee = cash_fee;
  }

  public int getCash_refund_fee() {
    return cash_refund_fee;
  }

  public void setCash_refund_fee(int cash_refund_fee) {
    this.cash_refund_fee = cash_refund_fee;
  }

  public String getCoupon_type_$n() {
    return coupon_type_$n;
  }

  public void setCoupon_type_$n(String coupon_type_$n) {
    this.coupon_type_$n = coupon_type_$n;
  }

  public int getCoupon_refund_fee_$n() {
    return coupon_refund_fee_$n;
  }

  public void setCoupon_refund_fee_$n(int coupon_refund_fee_$n) {
    this.coupon_refund_fee_$n = coupon_refund_fee_$n;
  }

  public int getCoupon_refund_count_$n() {
    return coupon_refund_count_$n;
  }

  public void setCoupon_refund_count_$n(int coupon_refund_count_$n) {
    this.coupon_refund_count_$n = coupon_refund_count_$n;
  }

  public String getCoupon_refund_batch_id_$n_$m() {
    return coupon_refund_batch_id_$n_$m;
  }

  public void setCoupon_refund_batch_id_$n_$m(String coupon_refund_batch_id_$n_$m) {
    this.coupon_refund_batch_id_$n_$m = coupon_refund_batch_id_$n_$m;
  }

  public String getCoupon_refund_id_$n_$m() {
    return coupon_refund_id_$n_$m;
  }

  public void setCoupon_refund_id_$n_$m(String coupon_refund_id_$n_$m) {
    this.coupon_refund_id_$n_$m = coupon_refund_id_$n_$m;
  }

  public int getCoupon_refund_fee_$n_$m() {
    return coupon_refund_fee_$n_$m;
  }

  public void setCoupon_refund_fee_$n_$m(int coupon_refund_fee_$n_$m) {
    this.coupon_refund_fee_$n_$m = coupon_refund_fee_$n_$m;
  }

  public WxData_RefundRes() {
  }

}