package com.emis.vi.pay.wxpay;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 微信付款-统一下单: 请求参数
 * author: Harry
 * Date: 2016/06/22
 * Time: 上午10:40
 * 接口文档地址:
 * 商户： https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1
 * 服务商： https://pay.weixin.qq.com/wiki/doc/api/jsapi_sl.php?chapter=9_1
 * <p/>
 * 接口调用: https://api.mch.weixin.qq.com/pay/unifiedorder
 */
public class WxData_UnifiedOrder {

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
   * 终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
   */
  private String device_info = "WEB";
  /**
   * 随机字符串  (必填)
   */
  private String nonce_str;
  /**
   * 签名  (必填)
   * 签名生成算法产生
   */
  private String sign;
  /**
   * 商品描述  (必填)
   * 商品或支付单简要描述
   */
  private String body;
  /**
   * 商品详情
   * 商品名称明细列表
   */
  private String detail;
  /**
   * 附加数据
   * 附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
   */
  private String attach;
  /**
   * 商户订单号  (必填)
   * 商户系统内部的订单号,32个字符内、可包含字母
   */
  private String out_trade_no;
  /**
   * 货币类型
   * 符合ISO 4217标准的三位字母代码，默认人民币：CNY
   */
  private String fee_type;
  /**
   * 总金额  (必填)
   * 订单总金额，单位为分
   */
  private int total_fee = 0;
  /**
   * 终端IP  (必填)
   * APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP
   */
  private String spbill_create_ip;
  /**
   * 交易起始时间
   * 订单生成时间，格式为yyyyMMddHHmmss
   */
  private String time_start;
  /**
   * 交易结束时间
   * 订单失效时间，格式为yyyyMMddHHmmss
   * 注意：最短失效时间间隔必须大于5分钟
   */
  private String time_expire;
  /**
   * 商品标记
   * 代金券或立减优惠功能的参数
   */
  private String goods_tag;
  /**
   * 通知地址  (必填)
   * 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
   */
  private String notify_url;
  /**
   * 交易类型  (必填)
   * 取值如下：JSAPI，NATIVE，APP
   */
  private String trade_type = "JSAPI";
  /**
   * 商品ID
   * trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。
   */
  private String product_id;
  /**
   * 指定支付方式
   * no_credit--指定不能使用信用卡支付
   */
  private String limit_pay;
  /**
   * 用户标识
   * trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。
   */
  private String openid;
  /**
   * 用户子标识
   * trade_type=JSAPI，此参数必传，用户在子商户appid下的唯一标识。
   * openid和sub_openid可以选传其中之一，如果选择传sub_openid,则必须传sub_appid。
   * 下单前需要调用【网页授权获取用户信息】接口获取到用户的Openid。
   */
  private String sub_openid;


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

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getDetail() {
    return detail;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }

  public String getAttach() {
    return attach;
  }

  public void setAttach(String attach) {
    this.attach = attach;
  }

  public String getOut_trade_no() {
    return out_trade_no;
  }

  public void setOut_trade_no(String out_trade_no) {
    this.out_trade_no = out_trade_no;
  }

  public String getFee_type() {
    return fee_type;
  }

  public void setFee_type(String fee_type) {
    this.fee_type = fee_type;
  }

  public int getTotal_fee() {
    return total_fee;
  }

  public void setTotal_fee(int total_fee) {
    this.total_fee = total_fee;
  }

  public String getSpbill_create_ip() {
    return spbill_create_ip;
  }

  public void setSpbill_create_ip(String spbill_create_ip) {
    this.spbill_create_ip = spbill_create_ip;
  }

  public String getTime_start() {
    return time_start;
  }

  public void setTime_start(String time_start) {
    this.time_start = time_start;
  }

  public String getTime_expire() {
    return time_expire;
  }

  public void setTime_expire(String time_expire) {
    this.time_expire = time_expire;
  }

  public String getGoods_tag() {
    return goods_tag;
  }

  public void setGoods_tag(String goods_tag) {
    this.goods_tag = goods_tag;
  }

  public String getNotify_url() {
    return notify_url;
  }

  public void setNotify_url(String notify_url) {
    this.notify_url = notify_url;
  }

  public String getTrade_type() {
    return trade_type;
  }

  public void setTrade_type(String trade_type) {
    this.trade_type = trade_type;
  }

  public String getProduct_id() {
    return product_id;
  }

  public void setProduct_id(String product_id) {
    this.product_id = product_id;
  }

  public String getLimit_pay() {
    return limit_pay;
  }

  public void setLimit_pay(String limit_pay) {
    this.limit_pay = limit_pay;
  }

  public String getOpenid() {
    return openid;
  }

  public void setOpenid(String openid) {
    this.openid = openid;
  }

  public String getSub_openid() {
    return sub_openid;
  }

  public void setSub_openid(String sub_openid) {
    this.sub_openid = sub_openid;
  }

  public WxData_UnifiedOrder() {

  }

  /**
   * 实例化（必填栏位）
   *
   * @param appid            公众账号ID
   * @param mch_id           商户号
   * @param body             商品描述
   * @param out_trade_no     商户订单号
   * @param total_fee        总金额(分)
   * @param spbill_create_ip 终端IP
   * @param notify_url       通知地址
   * @param openid           用户标识
   * @param key              微信商户平台(pay.weixin.qq.com)-->账户中心-->账户设置-->API安全-->密钥设置
   */
  @Deprecated
  public WxData_UnifiedOrder(String appid, String mch_id, String body, String out_trade_no, int total_fee, String spbill_create_ip, String notify_url, String openid, String key) {
    this.appid = appid;
    this.mch_id = mch_id;
    this.nonce_str = WxPayUtils.getNonce_str();
    this.body = body;
    this.out_trade_no = out_trade_no;
    this.total_fee = total_fee;
    this.spbill_create_ip = spbill_create_ip;
    this.notify_url = notify_url;
    this.openid = openid;
    createSign(key);
  }

  /**
   * 实例化（必填栏位）
   *
   * @param appid            公众账号ID
   * @param mch_id           商户号
   * @param body             商品描述
   * @param out_trade_no     商户订单号
   * @param total_fee        总金额(分)
   * @param spbill_create_ip 终端IP
   * @param notify_url       通知地址
   * @param openid           用户标识
   * @param key              微信商户平台(pay.weixin.qq.com)-->账户中心-->账户设置-->API安全-->密钥设置
   * @param sub_mch_id       微信支付分配的子商户号
   */
  public WxData_UnifiedOrder(String appid, String mch_id, String body, String out_trade_no, int total_fee, String spbill_create_ip, String notify_url, String openid, String key, String sub_mch_id) {
    this.appid = appid;
    this.mch_id = mch_id;
    this.nonce_str = WxPayUtils.getNonce_str();
    this.body = body;
    this.out_trade_no = out_trade_no;
    this.total_fee = total_fee;
    this.spbill_create_ip = spbill_create_ip;
    this.notify_url = notify_url;
    this.openid = openid;
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
        if (obj != null && !"".equals(field.getName()) && !"sign".equals(field.getName())) {
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