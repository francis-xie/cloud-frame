package com.emis.vi.pay.wxpay;


import com.tencent.bridge.IBridge;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-12-2
 * Time: 上午8:42
 * To change this template use File | Settings | File Templates.
 */
public class BridgeForScanPayBusiness implements IBridge {
  private String authCode;
  private String outTradeNo;
  private String body;
  private String attach;
  private int totalFee;
  private String deviceInfo;
  private String userIp;
  private String spBillCreateIP;
  private String timeStart;
  private String timeExpire;
  private String goodsTag;
  private String transactionID;
  private String outRefundNo;
  private int refundFee;
  private String refundID;
  private String billDate;
  private String billType;
  private String opUserID;
  private String refundFeeType;
  private String service;
  /**
   * 获取goods_tag:商品标记，微信平台配置的商品标记，用于优惠券或者满减使用
   * @return 商品标记
   */
  public String getGoodsTag() {
    return goodsTag;
  }

  public void setGoodsTag(String goodsTag) {
    this.goodsTag = goodsTag;
  }
  /**
   * 获取auth_code，这个是扫码终端设备从用户手机上扫取到的支付授权号，这个号是跟用户用来支付的银行卡绑定的，有效期是1分钟
   * @return 授权码
   */
  public String getAuthCode() {
    return authCode;
  }

  public void setAuthCode(String authCode) {
    this.authCode = authCode;
  }

  /**
   * 获取out_trade_no，这个是商户系统内自己可以用来唯一标识该笔订单的字符串，可以包含字母和数字，不超过32位
   * @return 订单号
   */
  public String getOutTradeNo() {
    return outTradeNo;
  }

  public void setOutTradeNo(String outTradeNo) {
    this.outTradeNo = outTradeNo;
  }

  /**
   * 获取body:要支付的商品的描述信息，用户会在支付成功页面里看到这个信息
   * @return 描述信息
   */
  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  /**
   * 获取attach:支付订单里面可以填的附加数据，API会将提交的这个附加数据原样返回，有助于商户自己可以注明该笔消费的具体内容，方便后续的运营和记录
   * @return 附加数据
   */
  public String getAttach() {
    return attach;
  }

  public void setAttach(String attach) {
    this.attach = attach;
  }
  /**
   * 获取订单总额
   * @return 订单总额
   */
  public int getTotalFee() {
    return totalFee;
  }

  public void setTotalFee(int totalFee) {
    this.totalFee = totalFee;
  }
  /**
   * 获取device_info:商户自己定义的扫码支付终端设备号，方便追溯这笔交易发生在哪台终端设备上
   * @return 支付终端设备号
   */
  public String getDeviceInfo() {
    return deviceInfo;
  }

  public void setDeviceInfo(String deviceInfo) {
    this.deviceInfo = deviceInfo;
  }
  /**
   * 获取userip:终端设备的ip地址
   * @return 终端设备的ip地址
   */
  public String getUserIp() {
    return userIp;
  }

  public void setUserIp(String userIp) {
    this.userIp = userIp;
  }
  /**
   * 获取spBillCreateIP:订单生成的机器IP
   * @return 订单生成的机器IP
   */
  public String getSpBillCreateIP() {
    return spBillCreateIP;
  }

  public void setSpBillCreateIP(String spBillCreateIP) {
    this.spBillCreateIP = spBillCreateIP;
  }
  /**
   * 获取time_start:订单生成时间，格式为yyyyMMddHHmmss，如2009年12 月25 日9 点10 分10 秒表示为20091225091010。时区为GMT+8 beijing。该时间取自商户服务器
   * @return 订单生成时间
   */
  public String getTimeStart() {
    //订单生成时间自然就是当前服务器系统时间咯
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    return simpleDateFormat.format(new Date());
  }

  public void setTimeStart(String timeStart) {
    this.timeStart = timeStart;
  }

  /**
   * 获取time_end:订单生成时间
   * @return 订单失效时间
   */
  public String getTimeExpire() {
    //订单失效时间，这个每个商户有自己的过期原则，我这里设置的是10天后过期
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    Calendar c = Calendar.getInstance();
    c.setTime(new Date());
    c.add(Calendar.DAY_OF_MONTH,10);
    return simpleDateFormat.format(c.getTime());
  }

  public void setTimeExpire(String timeExpire) {
    this.timeExpire = timeExpire;
  }

  public String getTransactionID() {
    return transactionID;
  }

  public void setTransactionID(String transactionID) {
    this.transactionID = transactionID;
  }

  public String getOutRefundNo() {
    return outRefundNo;
  }

  public void setOutRefundNo(String outRefundNo) {
    this.outRefundNo = outRefundNo;
  }

  public int getRefundFee() {
    return refundFee;
  }

  public void setRefundFee(int refundFee) {
    this.refundFee = refundFee;
  }

  public String getRefundID() {
    return refundID;
  }

  public void setRefundID(String refundID) {
    this.refundID = refundID;
  }

  public String getBillDate() {
    return billDate;
  }

  public void setBillDate(String billDate) {
    this.billDate = billDate;
  }

  public String getBillType() {
    return billType;
  }

  public void setBillType(String billType) {
    this.billType = billType;
  }

  public String getOpUserID() {
    return opUserID;
  }

  public void setOpUserID(String opUserID) {
    this.opUserID = opUserID;
  }

  public String getRefundFeeType() {
    return refundFeeType;
  }

  public void setRefundFeeType(String refundFeeType) {
    this.refundFeeType = refundFeeType;
  }

  public String getService() {
    return service;
  }

  public void setService(String service) {
    this.service = service;
  }
}
