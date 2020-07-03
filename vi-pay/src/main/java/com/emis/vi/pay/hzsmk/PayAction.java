package com.emis.vi.pay.hzsmk;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import com.emis.vi.pay.hzsmk.util.ConstantUtil;
import com.emis.vi.pay.hzsmk.util.JsonUtils;
import com.emis.vi.pay.hzsmk.util.SmkSign;
import com.emis.vi.pay.hzsmk.util.http.HttpClientUtil;

/*
 * 市民卡码付对接
 * 2017/09/22 Francis.xie Modify 需求 #40506 [标准版-金点]市民卡接口调整
 */
public class PayAction {

  //打log用
  private Logger log = null;
  private String responseBody = "";
  private String signErrResult = "{\"code\":\"ZZZZ\",\"msg\":\"验签失败，详情请咨询\"}";

  public static void main(String[] args) throws Exception {
//        PayAction p = new PayAction();
//        p.qrcPay();
  }

  /**
   * 付款码支付接口
   *
   * @param appId        应用编号
   * @param merId        商户号
   * @param merStoreId   门店ID
   * @param orderId      接入方流水号
   * @param orderTm      接入方请求时间
   * @param txnTokenCode 二维码Token信息
   * @param txnDes       支付交易描述
   * @param txnAmt       支付金额
   * @param termCode     终端标志
   * @param pfx_path     市民卡验签证书的本地路径
   * @param pfx_pwd      市民卡验签证书的密码
   * @param url          请求接口URL
   */
  public void qrcPay(String appId, String merId, String merStoreId, String orderId, String orderTm, String txnTokenCode, String txnDes, String txnAmt, String termCode,
                     String pfx_path, String pfx_pwd, String url) {
    try {
      Map<String, Object> reqMap = new HashMap<String, Object>();
      reqMap.put("appId", appId);//应用编号
      reqMap.put("txnCode", "AM1002");//交易码，固定值：AM1002
      reqMap.put("signType", "RSA");//签名类型
      reqMap.put("merId", merId);//商户号
      reqMap.put("merStoreId", merStoreId);//门店Id
      reqMap.put("nonceStr", (Math.round(Math.random() * 1000000000)) + "");//随机串
      reqMap.put("orderId", orderId);//接入方流水号
      reqMap.put("orderTm", orderTm);//接入方请求时间
      reqMap.put("orderNo", orderId);//订单号，不超过32位
      reqMap.put("txnTokenCode", txnTokenCode);//二维码Token信息
      reqMap.put("txnDes", txnDes);//支付交易描述
      reqMap.put("txnAmt", txnAmt); //交易金额
      reqMap.put("termCode", termCode);//终端标志
      String signData = ConstantUtil.coverMap2String(reqMap);
      log.info("对参数按照key=value的格式，并按照参数名ASCII字典序排序生成字符串：");
      log.info("[ " + signData + " ]");
      String signAture = SmkSign.sign(signData, pfx_path, pfx_pwd);
      //System.out.println("签名值：[" + signAture + "]");
      reqMap.put("signAture", signAture);

      String params = JsonUtils.parseMapObjToJson(reqMap);
      String result = HttpClientUtil.sendMsgHTTP(params, url, "");
      setResponseBody(result);
    } catch (Exception e) {
      log.error(e, e);
    }
  }

  /**
   * 支付退款交易接口
   *
   * @param appId      应用编号
   * @param merId      商户号
   * @param orderId    接入方流水号
   * @param orderTm    接入方请求时间
   * @param orgOrderId 原接入方流水号
   * @param orgOrderTm 原接入方请求时间
   * @param txnAmt     退款金额
   * @param termCode   终端标志
   * @param pfx_path   市民卡验签证书的本地路径
   * @param pfx_pwd    市民卡验签证书的密码
   * @param url        请求接口URL
   */
  public void refund(String appId, String merId, String orgOrderId, String orgOrderTm, String txnAmt, String termCode,
                     String pfx_path, String pfx_pwd, String url) {
    try {
      Map<String, Object> reqMap = new HashMap<String, Object>();
      reqMap.put("appId", appId);//应用编号
      reqMap.put("txnCode", "AM1003");//交易码，固定值：AM1003
      reqMap.put("signType", "RSA");//签名类型
      reqMap.put("merId", merId);//商户号
      reqMap.put("nonceStr", (Math.round(Math.random() * 1000000000)) + "");//随机串
      String sDateTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
      reqMap.put("orderId", termCode + sDateTime);//接入方流水号32个字符内、可包含字母, 确保唯一
      reqMap.put("orderTm", sDateTime);//接入方请求时间，发送交易时间，格式[yyyyMMddHHmmss] ;
      reqMap.put("orgOrderId", orgOrderId);//原接入方流水号
      reqMap.put("orgOrderTm", orgOrderTm);//原接入方请求时间
      reqMap.put("txnAmt", txnAmt); //退款金额
      reqMap.put("termCode", termCode);//终端标志
      String signData = ConstantUtil.coverMap2String(reqMap);
      log.info("对参数按照key=value的格式，并按照参数名ASCII字典序排序生成字符串：");
      log.info("[ " + signData + " ]");
      String signAture = SmkSign.sign(signData, pfx_path, pfx_pwd);
      //System.out.println("签名值：[" + signAture + "]");
      reqMap.put("signAture", signAture);

      String params = JsonUtils.parseMapObjToJson(reqMap);
      String result = HttpClientUtil.sendMsgHTTP(params, url, "");
      setResponseBody(result);
    } catch (Exception e) {
      log.error(e, e);
    }
  }

  /**
   * 交易状态查询接口
   *
   * @param appId      应用编号
   * @param merId      商户号
   * @param orgOrderId 原接入方流水号>>2017.09.22号改为用商户订单号查询（原接入方流水号=订单号）
   * @param termCode   终端标志
   * @param pfx_path   市民卡验签证书的本地路径
   * @param pfx_pwd    市民卡验签证书的密码
   * @param url        请求接口URL
   */
  public void queryOrder(String appId, String merId, String orgOrderId, String termCode,
                         String pfx_path, String pfx_pwd, String url) {
    try {
      Map<String, Object> reqMap = new HashMap<String, Object>();
      reqMap.put("appId", appId);//应用编号
      reqMap.put("txnCode", "AM1004");//交易码，固定值：AM1004
      reqMap.put("signType", "RSA");//签名类型
      reqMap.put("merId", merId);//商户号
      String sDateTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
      reqMap.put("orderId", termCode + sDateTime);//接入方流水号32个字符内、可包含字母, 确保唯一
      reqMap.put("orderTm", sDateTime);//接入方请求时间，发送交易时间，格式[yyyyMMddHHmmss] ;
      // reqMap.put("orgOrderId", orgOrderId);//原接入方流水号>>2017.09.22号改为用商户订单号查询 orgOrderId参数拿掉
      reqMap.put("merOrderNo", orgOrderId);//被查询交易的订单号
      reqMap.put("termCode", termCode);//终端标志
      String signData = ConstantUtil.coverMap2String(reqMap);
      log.info("对参数按照key=value的格式，并按照参数名ASCII字典序排序生成字符串：");
      log.info("[ " + signData + " ]");
      String signAture = SmkSign.sign(signData, pfx_path, pfx_pwd);
      //System.out.println("签名值：[" + signAture + "]");
      reqMap.put("signAture", signAture);

      String params = JsonUtils.parseMapObjToJson(reqMap);
      String result = HttpClientUtil.sendMsgHTTP(params, url, "");
      setResponseBody(result);
    } catch (Exception e) {
      log.error(e, e);
    }
  }

  /**
   * 二维码申请接口
   *
   * @param appId     应用编号
   * @param merId     商户号
   * @param qrcTypeNo 二维码类型:01 个人付款码、02 个人收款码
   * @param userId    用户ID
   * @param userName  用户名
   * @param pfx_path  市民卡验签证书的本地路径
   * @param pfx_pwd   市民卡验签证书的密码
   * @param url       请求接口URL
   */
  public void qrcApply(String appId, String merId, String qrcTypeNo, String userId, String userName, String pfx_path, String pfx_pwd, String url) {
    try {
      //用户信息
      Map<String, Object> userMap = new HashMap<String, Object>();
      userMap.put("userId", userId);
      userMap.put("userName", userName);

      Map<String, Object> reqMap = new HashMap<String, Object>();
      reqMap.put("appId", appId);
      reqMap.put("txnCode", "QR1001");//交易码，固定值：QR1001
      reqMap.put("signType", "RSA");//签名类型
      reqMap.put("merId", merId);
      reqMap.put("qrcTypeNo", qrcTypeNo);
      reqMap.put("qrcTxnInfo", JsonUtils.parseMapObjToJson(userMap));//二维码信息
      String sDateTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
      reqMap.put("orderId", sDateTime);//接入方流水号
      reqMap.put("orderTm", sDateTime);//接入方请求时间
      String signData = ConstantUtil.coverMap2String(reqMap);
      log.info("对参数按照key=value的格式，并按照参数名ASCII字典序排序生成字符串：");
      log.info("[ " + signData + " ]");
      String signAture = SmkSign.sign(signData, pfx_path, pfx_pwd);
      //System.out.println("签名值：[" + signAture + "]");
      reqMap.put("signAture", signAture);

      String params = JsonUtils.parseMapObjToJson(reqMap);
      String result = HttpClientUtil.sendMsgHTTP(params, url, "");
      setResponseBody(result);
    } catch (Exception e) {
      log.error(e, e);
    }
  }

  public void setLog(Logger log) {
    this.log = log;
  }

  public String getResponseBody() {
    return responseBody;
  }

  public void setResponseBody(String responseBody) {
    if (responseBody != null && !"".equals(responseBody)) {
      log.info("API返回的数据如下：");
      log.info(responseBody);
      //返回码
      Map<String, Object> resultMap = JsonUtils.paseJsonToMap(responseBody);
      if ("00".equals(resultMap.get("code"))) {
        boolean flag = SmkSign.verifySignRSA(resultMap); //验签
        if (flag) {
          log.info("验签成功");
          this.responseBody = responseBody;
        } else {
          log.info("验签失败");
          this.responseBody = signErrResult;
        }
      } else {
        this.responseBody = responseBody;
      }
    }
  }

}
