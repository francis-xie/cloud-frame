package com.emis.vi.pay.verypay.bean.request;

/**
 * $Id$
 * 查询请求参数
 * 2018/02/02 Francis.xie Modify 需求 #42028 [客制-宜芝多]费睿
 */
public class OrderQueryRequestBean {

  /**
   * 门店编号
   */
  private String storeSn;

  /**
   * 终端编号
   */
  private String clientSn;

  /**
   * 下单返回的交易号
   */
  private String orderSn;

  /**
   * 商户订单号,order_sn、out_sn二选一,如果同时存在优先级:order_sn > out_sn
   */
  private String outSn;

  /**
   * 微信公众号appid，当微信商户绑定多个公众号，传入对应的appid则返回对应的openid(sub_buyer)
   */
  private String wechatAppId;

  public OrderQueryRequestBean() {
    super();
  }

  public OrderQueryRequestBean(String orderSn) {
    super();
    this.orderSn = orderSn;
  }

  public String getStoreSn() {
    return this.storeSn;
  }

  public void setStoreSn(String storeSn) {
    this.storeSn = storeSn;
  }

  public String getClientSn() {
    return this.clientSn;
  }

  public void setClientSn(String clientSn) {
    this.clientSn = clientSn;
  }

  public String getOrderSn() {
    return orderSn;
  }

  public void setOrderSn(String orderSn) {
    this.orderSn = orderSn;
  }

  public String getOutSn() {
    return outSn;
  }

  public void setOutSn(String outSn) {
    this.outSn = outSn;
  }

  public String getWechatAppId() {
    return this.wechatAppId;
  }

  public void setWechatAppId(String wechatAppId) {
    this.wechatAppId = wechatAppId;
  }
}