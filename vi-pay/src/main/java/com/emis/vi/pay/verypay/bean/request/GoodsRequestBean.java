package com.emis.vi.pay.verypay.bean.request;

/**
 * $Id$
 * 商品信息实体类
 * 2018/02/02 Francis.xie Modify 需求 #42028 [客制-宜芝多]费睿
 */
public class GoodsRequestBean {

  /*
   * 商品编码
   */
  private String goodsId;

  /*
   * 商品名称
   */
  private String goodsName;

  /*
   * 商品单价(单位:分)
   */
  private int price;

  /*
   * 购买数量
   */
  private int quantity;

  /*
   * 商户本地端（收银系统）单品优惠标识,VeryPay会根据此规则判定该单品是否继续享受支付的单品优惠活动
   */
  private String itemTag;

  /*
   * 商品描述信息
   */
  private String body;

  public String getGoodsId() {
    return goodsId;
  }

  public GoodsRequestBean() {
    super();
  }

  public GoodsRequestBean(String goodsId, String goodsName, int price, int quantity) {
    super();
    this.goodsId = goodsId;
    this.goodsName = goodsName;
    this.price = price;
    this.quantity = quantity;
  }

  public void setGoodsId(String goodsId) {
    this.goodsId = goodsId;
  }

  public String getGoodsName() {
    return goodsName;
  }

  public void setGoodsName(String goodsName) {
    this.goodsName = goodsName;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public String getItemTag() {
    return itemTag;
  }

  public void setItemTag(String itemTag) {
    this.itemTag = itemTag;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }
}