package com.emis.vi.pay.verypay.bean.request;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * $Id$
 * 支付请求参数
 * 2018/02/02 Francis.xie Modify 需求 #42028 [客制-宜芝多]费睿
 */
public class MicropayRequestBean {

  /**
   * 门店编号
   */
  private String storeSn;

  /**
   * 终端编号
   */
  private String clientSn;

  /**
   * 扫码支付授权码,设备读取用户的条码或者二维码信息
   */
  private String authCode;

  /**
   * 订单总金额,只能为整数,单位人民币(分)
   */
  private int totalFee;

  /**
   * 商户销售订单号,仅能可包含字母和数字,当同一业务订单需要进行多次支付(前几次失败或被撤销)时,需要保证每次调用接口传入的商户订单号out_sn不重复,详情见文档说明
   */
  private String outSn;

  /**
   * 支付宝不打折金额,不能大于订单总金额,留空或者为0则全部订单金额参与打折,微信该参数无效,单位人民币(分)
   */
  private int unDiscountFee;

  /**
   * 支付渠道,默认建议不要传该字段,后端会自动识别支付方式,如果确实需要限定本次支付方式可以传对应的值,详情见文档说明
   */
  private String channel;

  /**
   * 商品或支付单简要描述,默认为门店名+终端名
   */
  private String body;

  /**
   * 订单失效时间,默认是7200秒
   */
  private int timeExpire;

  /**
   * 商品标记,代金券或立减优惠功能的参数,详情见文档说明
   */
  private String goodsTag;

  /**
   * 商户信息(JSON字符串,可以包含多个商品信息)
   */
  //private List<GoodsRequestBean> goodsDetail;
  private String goodsDetail;

  /**
   * 微信公众号appId,仅在微信支付商户后台配置了多个公众号的情况下需要传
   */
  private int wechatAppId;

  /**
   * string(32) 商品编码
   */
  private String goodsId;

  /**
   * string(256) 商品名称
   */
  private String goodsName;

  public MicropayRequestBean() {
    super();
  }

  public MicropayRequestBean(String storeSn, String clientSn, String authCode, int totalFee, String outSn) {
    super();
    this.storeSn = storeSn;
    this.clientSn = clientSn;
    this.authCode = authCode;
    this.totalFee = totalFee;
    this.outSn = outSn;
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

  public String getAuthCode() {
    return authCode;
  }

  public void setAuthCode(String authCode) {
    this.authCode = authCode;
  }

  public int getTotalFee() {
    return totalFee;
  }

  public void setTotalFee(int totalFee) {
    this.totalFee = totalFee;
  }

  public String getOutSn() {
    return outSn;
  }

  public void setOutSn(String outSn) {
    this.outSn = outSn;
  }

  public int getUnDiscountFee() {
    return unDiscountFee;
  }

  public void setUnDiscountFee(int unDiscountFee) {
    this.unDiscountFee = unDiscountFee;
  }

  public String getChannel() {
    return channel;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public int getTimeExpire() {
    return timeExpire;
  }

  public void setTimeExpire(int timeExpire) {
    this.timeExpire = timeExpire;
  }

  public String getGoodsTag() {
    return goodsTag;
  }

  public void setGoodsTag(String goodsTag) {
    this.goodsTag = goodsTag;
  }
/*
  */

  /**
   * goods_detail参数
   * :!: 特别提示，如果有参与单品优惠活动的订单，当有退款发生时，必须整单退款
   *
   * @return
   *//*
  public String getGoodsDetail() {
    JsonArray jsonArray = new JsonArray();
    if (goodsDetail != null && goodsDetail.size() > 0) {
      for (int i = 0; i < goodsDetail.size(); i++) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("goods_id", goodsDetail.get(i).getGoodsId()); //string(32)	商品编码
        jsonObject.addProperty("goods_name", goodsDetail.get(i).getGoodsName()); //string(256)	 商品名称
        jsonObject.addProperty("price", goodsDetail.get(i).getPrice()); //int 商品单价(单位:分)
        jsonObject.addProperty("quantity", goodsDetail.get(i).getQuantity()); //int 购买数量
        //string(128)	 商户本地端（收银系统）单品优惠标识，VeryPay会根据此规则判定该单品是否继续享受支付的单品优惠活动
        if (goodsDetail.get(i).getItemTag() != null)
          jsonObject.addProperty("item_tag", goodsDetail.get(i).getItemTag());
        if (goodsDetail.get(i).getBody() != null)
          jsonObject.addProperty("body", goodsDetail.get(i).getBody()); //string(6000)	商品描述信息
        jsonArray.add(jsonObject);
      }
    }
    return jsonArray.toString();
  }

  public void setGoodsDetail(List<GoodsRequestBean> list) {
    this.goodsDetail = list;
  }*/
  public String getGoodsDetail() {
    return this.goodsDetail;
  }

  public void setGoodsDetail(String goodsDetail) {
    this.goodsDetail = goodsDetail;
  }

  public int getWechatAppId() {
    return wechatAppId;
  }

  public void setWechatAppId(int wechatAppId) {
    this.wechatAppId = wechatAppId;
  }

  public String getGoodsName() {
    return this.goodsName;
  }

  public void setGoodsName(String goodsName) {
    this.goodsName = goodsName;
  }

  public String getGoodsId() {
    return this.goodsId;
  }

  public void setGoodsId(String goodsId) {
    this.goodsId = goodsId;
  }
}