package com.emis.vi.pay.wxpay;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信付款-申请退款: 请求参数
 * author: Harry
 * Date: 2016/06/27
 * Time: 下午4:29
 * 接口文档地址:
 * 商户： https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_4
 * 服务商： https://pay.weixin.qq.com/wiki/doc/api/jsapi_sl.php?chapter=9_4
 * <p/>
 * 权限申请
 * 服务商模式下，退款接口需要单独申请权限，指引链接：http://kf.qq.com/faq/120911VrYVrA150929imAfuU.html
 * <p/>
 * 接口调用: https://api.mch.weixin.qq.com/secapi/pay/refund
 */
public class WxData_Refund {

  /**
   * 公众账号ID  (必填)
   * 微信分配的公众账号ID（企业号corpid即为此appId）
   */
  private String appid;
  /**
   * 商户号  (必填)
   * 微信支付分配的商户号
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
   * 随机字符串  (必填)
   * 随机字符串，不长于32位。
   */
  private String nonce_str;
  /**
   * 签名  (必填)
   * 签名生成算法产生
   */
  private String sign;
  /**
   * 微信订单号  (和【商户订单号】二选一)
   */
  private String transaction_id;
  /**
   * 商户订单号  (和【微信订单号】二选一)
   * 商户系统内部的订单号，当没提供transaction_id时需要传这个。
   */
  private String out_trade_no;
  /**
   * 商户退款单号  (必填)
   * 商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
   */
  private String out_refund_no;
  /**
   * 总金额  (必填)
   */
  private int total_fee;
  /**
   * 退款金额  (必填)
   */
  private int refund_fee;
  /**
   * 货币种类
   */
  private String refund_fee_type;
  /**
   * 操作员  (必填)
   * 操作员帐号, 默认为商户号
   */
  private String op_user_id;
  /**
   * 退款资金来源
   * 仅针对老资金流商户使用
   * REFUND_SOURCE_UNSETTLED_FUNDS---未结算资金退款（默认使用未结算资金退款）
   * REFUND_SOURCE_RECHARGE_FUNDS---可用余额退款（限非当日交易订单的退款）
   */
  private String refund_account;

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

  public int getTotal_fee() {
    return total_fee;
  }

  public void setTotal_fee(int total_fee) {
    this.total_fee = total_fee;
  }

  public int getRefund_fee() {
    return refund_fee;
  }

  public void setRefund_fee(int refund_fee) {
    this.refund_fee = refund_fee;
  }

  public String getRefund_fee_type() {
    return refund_fee_type;
  }

  public void setRefund_fee_type(String refund_fee_type) {
    this.refund_fee_type = refund_fee_type;
  }

  public String getOp_user_id() {
    return op_user_id;
  }

  public void setOp_user_id(String op_user_id) {
    this.op_user_id = op_user_id;
  }

  public String getRefund_account() {
    return refund_account;
  }

  public void setRefund_account(String refund_account) {
    this.refund_account = refund_account;
  }

  public WxData_Refund() {

  }

  /**
   *
   * @param appid           公众账号ID
   * @param mch_id          商户号
   * @param transaction_id  微信订单号(二选一)
   * @param out_trade_no    商户订单号(二选一)
   * @param out_refund_no   商户退款单号
   * @param total_fee       总金额
   * @param refund_fee      退款金额
   * @param op_user_id      操作员
   * @param key             微信商户平台(pay.weixin.qq.com)-->账户中心-->账户设置-->API安全-->密钥设置
   */
  @Deprecated
  public WxData_Refund(String appid, String mch_id, String transaction_id, String out_trade_no, String out_refund_no, int total_fee, int refund_fee, String op_user_id, String key) {
    this.appid = appid;
    this.mch_id = mch_id;
    this.transaction_id = transaction_id;
    this.out_trade_no = out_trade_no;
    this.out_refund_no = out_refund_no;
    this.total_fee = total_fee;
    this.refund_fee = refund_fee;
    this.op_user_id = op_user_id;
    this.nonce_str = WxPayUtils.getNonce_str();
    createSign(key);
  }

  /**
   *
   * @param appid           公众账号ID
   * @param mch_id          商户号
   * @param transaction_id  微信订单号(二选一)
   * @param out_trade_no    商户订单号(二选一)
   * @param out_refund_no   商户退款单号
   * @param total_fee       总金额
   * @param refund_fee      退款金额
   * @param op_user_id      操作员
   * @param key             微信商户平台(pay.weixin.qq.com)-->账户中心-->账户设置-->API安全-->密钥设置
   */
  public WxData_Refund(String appid, String mch_id, String transaction_id, String out_trade_no, String out_refund_no
      , int total_fee, int refund_fee, String op_user_id, String key, String sub_mch_id) {
    this.appid = appid;
    this.mch_id = mch_id;
    this.transaction_id = transaction_id;
    this.out_trade_no = out_trade_no;
    this.out_refund_no = out_refund_no;
    this.total_fee = total_fee;
    this.refund_fee = refund_fee;
    this.op_user_id = op_user_id;
    this.nonce_str = WxPayUtils.getNonce_str();
    this.sub_mch_id = sub_mch_id;
    createSign(key);
  }

   /**
   * 获取签名  (必填)
   * 签名生成算法产生
   *
   * @param key 微信商户平台(pay.weixin.qq.com)-->账户中心-->账户设置-->API安全-->密钥设置
   * @return 签名
   */
  public String createSign(String key) {
    sign = WxPayUtils.getSign(toMap(), key);
    return sign;
  }

  /**
   * 获取父类的所有字段
   *
   * @return
   */
  private Map<String, Object> toMap() {
    Map<String, Object> map = new HashMap<String, Object>();
    Class<?> clazz = this.getClass();
    Field[] fields = clazz.getDeclaredFields();
//    System.out.println("fieldsSize=" + fields.length);
    for (Field field : fields) {
      Object obj;
      try {
        //设置字段访问权限,私有属性才可以获取
        field.setAccessible(true);
        obj = field.get(this);
        if (obj != null && !"".equals(field.getName()) && !"sign".equals(field.getName()) ) {
//          System.out.println(field.getName() + "=" + obj.toString());
          map.put(field.getName(), obj);
        }
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return map;
  }

}