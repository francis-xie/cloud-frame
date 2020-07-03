package com.emis.vi.pay.wxpay;

/**
 * 微信付款-查询订单: 返回结果
 * author: Harry
 * Date: 2016/06/27
 * Time: 下午2:20
 * 接口文档地址:
 * 商户： https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1
 * 服务商： https://pay.weixin.qq.com/wiki/doc/api/jsapi_sl.php?chapter=9_1
 * <p/>
 * 接口调用: https://api.mch.weixin.qq.com/pay/unifiedorder
 */
public class WxData_OrderQueryRes {

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
   * 设备号
   * 微信支付分配的终端设备号
   */
  private String device_info;
  /**
   * 用户标识
   */
  private String openid;
  /**
   * 是否关注公众账号
   * Y-关注，N-未关注
   */
  private String is_subscribe;
  /**
   * 用户子标识
   * 用户在子商户appid下的唯一标识
   */
  private String sub_openid;
  /**
   * 是否关注子公众账号
   * 用户是否关注子公众账号，Y-关注，N-未关注，仅在公众账号类型支付有效
   */
  private String sub_is_subscribe;
  /**
   * 交易类型
   * 调用接口提交的交易类型，取值如下：JSAPI，NATIVE，APP，MICROPAY
   */
  private String trade_type;
  /**
   * 交易状态
   * SUCCESS—支付成功
   REFUND—转入退款
   NOTPAY—未支付
   CLOSED—已关闭
   REVOKED—已撤销（刷卡支付）
   USERPAYING--用户支付中
   PAYERROR--支付失败(其他原因，如银行返回失败)
   */
  private String trade_state;
  /**
   * 付款银行
   * 银行类型，采用字符串类型的银行标识
   */
  private String bank_type;
  /**
   * 商品详情
   */
  private String detail;
  /**
   * 订单金额
   * 订单总金额，单位为分
   */
  private int total_fee;
  /**
   * 应结订单金额
   * 应结订单金额=订单金额-非充值代金券金额，应结订单金额<=订单金额。
   */
  private int settlement_total_fee;
  /**
   * 货币种类
   * 默认人民币：CNY
   */
  private String fee_type;
  /**
   * 现金支付金额
   */
  private int cash_fee;
  /**
   * 现金支付货币类型
   */
  private String cash_fee_type;
  /**
   * 代金券金额
   * “代金券”金额<=订单金额，订单金额-“代金券”金额=现金支付金额，
   */
  private int coupon_fee;
  /**
   * 代金券使用数量
   */
  private int coupon_count;
  /**
   * 代金券批次ID
   */
  private String coupon_batch_id_$n;
  /**
   * 代金券类型
   */
  private int coupon_type_$n;
  /**
   * 代金券ID
   */
  private String coupon_id_$n;
  /**
   * 单个代金券支付金额
   */
  private int coupon_fee_$n;
  /**
   * 微信支付订单号
   */
  private String transaction_id;
  /**
   * 商户订单号
   */
  private String out_trade_no;
  /**
   * 附加数据
   * 附加数据，原样返回
   */
  private String attach;
  /**
   * 支付完成时间
   * 订单支付时间，格式为yyyyMMddHHmmss
   */
  private String time_end;
  /**
   * 交易状态描述
   */
  private String trade_state_desc;

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

  public String getDevice_info() {
    return device_info;
  }

  public void setDevice_info(String device_info) {
    this.device_info = device_info;
  }

  public String getOpenid() {
    return openid;
  }

  public void setOpenid(String openid) {
    this.openid = openid;
  }

  public String getIs_subscribe() {
    return is_subscribe;
  }

  public void setIs_subscribe(String is_subscribe) {
    this.is_subscribe = is_subscribe;
  }

  public String getSub_openid() {
    return sub_openid;
  }

  public void setSub_openid(String sub_openid) {
    this.sub_openid = sub_openid;
  }

  public String getSub_is_subscribe() {
    return sub_is_subscribe;
  }

  public void setSub_is_subscribe(String sub_is_subscribe) {
    this.sub_is_subscribe = sub_is_subscribe;
  }

  public String getTrade_type() {
    return trade_type;
  }

  public void setTrade_type(String trade_type) {
    this.trade_type = trade_type;
  }

  public String getTrade_state() {
    return trade_state;
  }

  public void setTrade_state(String trade_state) {
    this.trade_state = trade_state;
  }

  public String getBank_type() {
    return bank_type;
  }

  public void setBank_type(String bank_type) {
    this.bank_type = bank_type;
  }

  public String getDetail() {
    return detail;
  }

  public void setDetail(String detail) {
    this.detail = detail;
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

  public String getCash_fee_type() {
    return cash_fee_type;
  }

  public void setCash_fee_type(String cash_fee_type) {
    this.cash_fee_type = cash_fee_type;
  }

  public int getCoupon_fee() {
    return coupon_fee;
  }

  public void setCoupon_fee(int coupon_fee) {
    this.coupon_fee = coupon_fee;
  }

  public int getCoupon_count() {
    return coupon_count;
  }

  public void setCoupon_count(int coupon_count) {
    this.coupon_count = coupon_count;
  }

  public String getCoupon_batch_id_$n() {
    return coupon_batch_id_$n;
  }

  public void setCoupon_batch_id_$n(String coupon_batch_id_$n) {
    this.coupon_batch_id_$n = coupon_batch_id_$n;
  }

  public int getCoupon_type_$n() {
    return coupon_type_$n;
  }

  public void setCoupon_type_$n(int coupon_type_$n) {
    this.coupon_type_$n = coupon_type_$n;
  }

  public String getCoupon_id_$n() {
    return coupon_id_$n;
  }

  public void setCoupon_id_$n(String coupon_id_$n) {
    this.coupon_id_$n = coupon_id_$n;
  }

  public int getCoupon_fee_$n() {
    return coupon_fee_$n;
  }

  public void setCoupon_fee_$n(int coupon_fee_$n) {
    this.coupon_fee_$n = coupon_fee_$n;
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

  public String getAttach() {
    return attach;
  }

  public void setAttach(String attach) {
    this.attach = attach;
  }

  public String getTime_end() {
    return time_end;
  }

  public void setTime_end(String time_end) {
    this.time_end = time_end;
  }

  public String getTrade_state_desc() {
    return trade_state_desc;
  }

  public void setTrade_state_desc(String trade_state_desc) {
    this.trade_state_desc = trade_state_desc;
  }

  public WxData_OrderQueryRes() {
  }

}
