package com.emis.vi.pay.wxpay;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 微信付款-查询订单: 请求参数
 * author: Harry
 * Date: 2016/06/27
 * Time: 下午2:20
 * https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_2
 * 接口文档地址:
 * 商户： https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_2
 * 服务商： https://pay.weixin.qq.com/wiki/doc/api/jsapi_sl.php?chapter=9_2
 * <p/>
 * 接口调用: https://api.mch.weixin.qq.com/pay/orderquery
 */
public class WxData_OrderQuery {

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
   * 如需在支付完成后获取sub_openid则此参数必传。
   */
  private String sub_appid;
  /**
   * 微信支付分配的子商户号
   */
  private String sub_mch_id;
  /**
   * 微信订单号  (和【商户订单号】二选一)
   * 微信的订单号，优先使用
   */
  private String transaction_id;
  /**
   * 商户订单号  (和【微信订单号】二选一)
   * 商户系统内部的订单号，当没提供transaction_id时需要传这个。
   */
  private String out_trade_no;
  /**
   * 随机字符串
   * 随机字符串，不长于32位。
   */
  private String nonce_str;
  /**
   * 签名  (必填)
   * 签名生成算法产生
   */
  private String sign;

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

  public WxData_OrderQuery() {

  }

  /**
   * 实例化（必填栏位）
   * @param appid             公众账号ID
   * @param mch_id            商户号
   * @param transaction_id    微信订单号(二选一)
   * @param out_trade_no      商户订单号(二选一)
   * @param key               微信商户平台(pay.weixin.qq.com)-->账户中心-->账户设置-->API安全-->密钥设置
   */
  @Deprecated
  public WxData_OrderQuery(String appid, String mch_id, String transaction_id, String out_trade_no, String key) {
    this.appid = appid;
    this.mch_id = mch_id;
    this.transaction_id = transaction_id;
    this.out_trade_no = out_trade_no;
    this.nonce_str = WxPayUtils.getNonce_str();
    createSign(key);
  }

  /**
   * 实例化（必填栏位）
   * @param appid             公众账号ID
   * @param mch_id            商户号
   * @param transaction_id    微信订单号(二选一)
   * @param out_trade_no      商户订单号(二选一)
   * @param key               微信商户平台(pay.weixin.qq.com)-->账户中心-->账户设置-->API安全-->密钥设置
   * @param sub_mch_id        微信支付分配的子商户号
   */
  public WxData_OrderQuery(String appid, String mch_id, String transaction_id, String out_trade_no, String key, String sub_mch_id) {
    this.appid = appid;
    this.mch_id = mch_id;
    this.transaction_id = transaction_id;
    this.out_trade_no = out_trade_no;
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