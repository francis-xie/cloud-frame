package com.emis.vi.pay.verypay.bean.request;

/**
 * $Id$
 * 退款请求参数
 * 2018/02/02 Francis.xie Modify 需求 #42028 [客制-宜芝多]费睿
 */
public class RefundRequestBean {

  /**
   * 门店编号
   */
  private String storeSn;

  /**
   * 终端编号
   */
  private String clientSn;

  /*
   * 下单交易号
   */
  private String orderSn;

  /*
   * 商户订单号,order_sn、out_sn二选一,如果同时存在优先级:order_sn > out_sn
   */
  private String outSn;

  /*
   * 退款金额,单位分,不得大于订单总金额
   */
  private int refundFee;

  /*
   * 商户退款单号,由商户自己生成并保证唯一,同一退款单号多次请求只退一笔,当退款返回状态为 10013 的时候需要重新发起退款请求时,使用原商户退款单号
   */
  private String refundOutSn;

  /*
   * 操作员编号,如果后台设置了默认操作员,可不传
   */
  private String opUserId;

  /*
   * 操作员密码,如果后台设置了默认操作员,可不传
   */
  private String opUserPwd;

  public RefundRequestBean() {
    super();
  }

  public RefundRequestBean(String storeSn, String clientSn, String orderSn, int refundFee, String refundOutSn) {
    super();
    this.storeSn = storeSn;
    this.clientSn = clientSn;
    this.orderSn = orderSn;
    this.refundFee = refundFee;
    this.refundOutSn = refundOutSn;
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

  public int getRefundFee() {
    return refundFee;
  }

  public void setRefundFee(int refundFee) {
    this.refundFee = refundFee;
  }

  public String getRefundOutSn() {
    return refundOutSn;
  }

  public void setRefundOutSn(String refundOutSn) {
    this.refundOutSn = refundOutSn;
  }

  public String getOpUserId() {
    return opUserId;
  }

  public void setOpUserId(String opUserId) {
    this.opUserId = opUserId;
  }

  public String getOpUserPwd() {
    return opUserPwd;
  }

  public void setOpUserPwd(String opUserPwd) {
    this.opUserPwd = opUserPwd;
  }
}