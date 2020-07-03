package com.emis.vi.pay.verypay;

import com.emis.vi.pay.verypay.bean.request.CloseOrderRequestBean;
import com.emis.vi.pay.verypay.bean.request.MicropayRequestBean;
import com.emis.vi.pay.verypay.bean.request.OrderQueryRequestBean;
import com.emis.vi.pay.verypay.bean.request.RefundRequestBean;
import com.emis.vi.pay.verypay.common.Signature;
import com.emis.vi.pay.verypay.common.Utils;
import com.emis.vi.pay.verypay.config.VeryPayConfig;
import com.emis.vi.pay.verypay.utils.GsonUtils;
import com.emis.vi.pay.verypay.utils.http.HttpClientUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * $Id: VeryPay.java 10902 2018-02-05 06:34:38Z francis.xie $
 * SDK总入口
 * 2018/02/02 Francis.xie Modify 需求 #42028 [客制-宜芝多]费睿
 */
public class VeryPay {

  private String responseBody = "";
  private String errResult = "{\"retcode\":\"999999\",\"msg\":\"处理异常,请联系管理人员!\"}";

  /**
   * 条码支付接口(原刷卡支付)
   * 接口说明
   * 收银员使用扫码设备读取手机上的二维码或条码信息传送至商户收银台，由商户收银台或者商户后台调用该接口发起支付
   * <p/>
   * URL	https://api.pay.verystar.cn/v3/pay/micropay.json
   * 请求方式	POST
   * 返回格式	JSON
   * 请求参数
   * :!: 特别提示，构造http请求发起支付：请注意需要对请求做urlencode处理（ 查看示例 ），以下请求示例是为了方便查看，而没有做urlencode的原始报文
   *
   * @param bean
   * @param requestUrl
   */
  public void micropay(MicropayRequestBean bean, String requestUrl) {
    try {
      if (!VeryPayConfig.verify())
        return;
      Map<String, Object> params = new HashMap<String, Object>();
      params.put("app_key", VeryPayConfig.getAppKey()); //string(32)	API分配的公钥,和私钥是一对，私钥用于请求加密
      params.put("time", System.currentTimeMillis()); //int当前请求时间戳(秒)，比如 1494571680
      params.put("store_sn", bean.getStoreSn()); //string(32)	门店编号
      params.put("client_sn", bean.getClientSn()); //string(32)	终端编号
      params.put("auth_code", bean.getAuthCode()); //string(32)	扫码支付授权码，设备读取用户的条码或者二维码信息
      params.put("total_fee", bean.getTotalFee()); //Int	 订单总金额，只能为整数，单位人民币（分）
      //string(64)	商户销售订单号，仅能可包含字母和数字，当同一业务订单需要进行多次支付（前几次失败或被撤销）时，需要保证每次调用接口传入的商户订单号out_sn不重复
      params.put("out_sn", bean.getOutSn());
      if (bean.getUnDiscountFee() != 0)
        params.put("undiscount_fee", bean.getUnDiscountFee()); //Int 支付宝不打折金额，不能大于订单总金额，留空或者为0则全部订单金额参与打折，微信该参数无效，单位人民币（分）
      if (bean.getChannel() != null && !"".equals(bean.getChannel()))
        params.put("channel", bean.getChannel()); //string(32)	支付渠道，默认建议不要传该字段，后端会自动识别支付方式，如果确实需要限定本次支付方式可以传对应的值
      if (bean.getBody() != null && !"".equals(bean.getBody()))
        params.put("body", bean.getBody()); //string(100) 商品或支付单简要描述，默认为门店名+终端名
      if (bean.getTimeExpire() != 0)
        params.put("time_expire", bean.getTimeExpire()); //int 订单失效时间，默认和最大值都是7200秒
      if (bean.getGoodsTag() != null && !"".equals(bean.getGoodsTag()))
        params.put("goods_tag", bean.getGoodsTag()); //string(100)	 商品标记，代金券或立减优惠功能的参数
      if (bean.getGoodsDetail() != null && !"".equals(bean.getGoodsDetail()) && !bean.getGoodsDetail().equals("[]")) {
        params.put("goods_detail", bean.getGoodsDetail()); //string(6000)		商户信息(json字符串，可以包含多个商品信息)
      } else if (bean.getGoodsId() != null && !"".equals(bean.getGoodsId()) && bean.getGoodsName() != null && !"".equals(bean.getGoodsName())) {
        params.put("goods_detail", "[{" + //商品信息(json字符串，可以包含多个商品信息)
          "\"goods_id\":\"" + bean.getGoodsId() + "\"," +  //string(32) 商品编码
          "\"goods_name\":\"" + bean.getGoodsName() + "\"," +  //string(256) 商品名称
          "\"price\":" + bean.getTotalFee() + "," + //int 商品单价(单位:分)
          "\"quantity\":1}]"); //int 购买数量
      }
      if (bean.getWechatAppId() != 0)
        params.put("wechat_app_id", bean.getWechatAppId()); //int 微信公众号appid,仅在微信支付商户后台配置了多个公众号的情况下需要传
      params.put("sign", Signature.getSign(params, VeryPayConfig.getAppSecret())); //string(32) 加密验证码

      if (VeryPayConfig.isDebug()) {
        VeryPayConfig.getLog().info(params.toString());
        VeryPayConfig.getLog().info(VeryPayConfig.getApiUrl() + requestUrl);
      }
      params = Utils.MapValueEncoder(params);
      VeryPayConfig.getLog().info(params.toString());
      String result = HttpClientUtil.sendMsgHTTP(params, VeryPayConfig.getApiUrl() + requestUrl);
      setResponseBody(result);
    } catch (Exception e) {
      this.responseBody = this.errResult;
      VeryPayConfig.getLog().error(e, e);
    }
  }

  /**
   * 订单查询接口
   * 接口说明
   * 该接口提供所有支付订单的查询，商户可以通过该接口主动查询订单状态，完成下一步的业务逻辑。
   * <p/>
   * URL	https://api.pay.verystar.cn/v3/pay/orderquery.json
   * 请求方式	POST
   * 返回格式	JSON
   *
   * @param bean
   * @param requestUrl
   */
  public void queryOrder(OrderQueryRequestBean bean, String requestUrl) {
    try {
      if (!VeryPayConfig.verify())
        return;
      Map<String, Object> params = new HashMap<String, Object>();
      params.put("app_key", VeryPayConfig.getAppKey()); //string(32)	API分配的公钥,和私钥是一对，私钥用于请求加密
      params.put("time", System.currentTimeMillis()); //int 当前请求时间戳(秒)，比如 1494571680
      params.put("store_sn", bean.getStoreSn()); //string(32)	门店编号
      params.put("client_sn", bean.getClientSn()); //string(32)	终端编号
      if (bean.getOrderSn() != null && !"".equals(bean.getOrderSn()))
        params.put("order_sn", bean.getOrderSn()); //string(32)	统一下单接口返回的商户单号
      if (bean.getOutSn() != null && !"".equals(bean.getOutSn()))
        params.put("out_sn", bean.getOutSn()); //string(64)	商户销售订单号，order_sn、out_sn二选一，如果同时存在优先级：order_sn > out_sn
      if (bean.getWechatAppId() != null && !"".equals(bean.getWechatAppId()))
        params.put("wechat_app_id", bean.getWechatAppId()); //string(64)	微信公众号appid，当微信商户绑定多个公众号，传入对应的appid则返回对应的openid(sub_buyer)
      params.put("sign", Signature.getSign(params, VeryPayConfig.getAppSecret())); //string(32) 加密验证码

      if (VeryPayConfig.isDebug()) {
        VeryPayConfig.getLog().info(params.toString());
        VeryPayConfig.getLog().info(VeryPayConfig.getApiUrl() + requestUrl);
      }
      params = Utils.MapValueEncoder(params);
      VeryPayConfig.getLog().info(params.toString());
      String result = HttpClientUtil.sendMsgHTTP(params, VeryPayConfig.getApiUrl() + requestUrl);
      setResponseBody(result);
    } catch (Exception e) {
      this.responseBody = this.errResult;
      VeryPayConfig.getLog().error(e, e);
    }
  }

  /**
   * 申请退款接口
   * 接口说明
   * 当交易发生之后一段时间内，由于买家或者卖家的原因需要退款时，卖家可以通过退款接口将支付款退还给买家，
   * Wechat将在收到退款请求并且验证成功之后，按照退款规则将支付款按原路退到买家帐号上。
   * <p/>
   * URL	https://api.pay.verystar.cn/v3/pay/refund.json
   * 请求方式	POST
   * 返回格式	JSON
   * 请求参数
   * :!: 特别提示，如果有参与单品优惠活动的订单，当有退款发生时，必须整单退款
   *
   * @param bean
   * @param requestUrl
   */
  public void refund(RefundRequestBean bean, String requestUrl) {
    try {
      if (!VeryPayConfig.verify())
        return;
      Map<String, Object> params = new HashMap<String, Object>();
      params.put("app_key", VeryPayConfig.getAppKey()); //string(32)	API分配的公钥,和私钥是一对，私钥用于请求加密
      params.put("time", System.currentTimeMillis()); //int 当前请求时间戳(秒)，比如 1494571680
      params.put("store_sn", bean.getStoreSn()); //string(32)	门店编号
      params.put("client_sn", bean.getClientSn()); //string(32)	终端编号
      if (bean.getOrderSn() != null && !"".equals(bean.getOrderSn()))
        params.put("order_sn", bean.getOrderSn()); //string(32)	统一下单接口返回的商户单号
      if (bean.getOutSn() != null && !"".equals(bean.getOutSn()))
        params.put("out_sn", bean.getOutSn()); //string(64)	商户销售订单号，order_sn、out_sn二选一，如果同时存在优先级：order_sn > out_sn
      params.put("refund_fee", bean.getRefundFee()); //int 退款金额,单位分，不得大于订单总金额
      //string(100)	商户退款单号，由商户自己生成并保证唯一，同一退款单号多次请求只退一笔，当退款返回状态为 10013 的时候需要重新发起退款请求时，使用原商户退款单号
      params.put("refund_out_sn", bean.getRefundOutSn());
      if (bean.getOpUserId() != null && !"".equals(bean.getOpUserId()))
        params.put("op_user_id", bean.getOpUserId()); //string(32) 操作员编号，如果后台设置了默认操作员，可不传
      if (bean.getOpUserPwd() != null && !"".equals(bean.getOpUserPwd()))
        params.put("op_user_pwd", bean.getOpUserPwd()); //string(32) 操作员密码，如果后台设置了默认操作员，可不传
      params.put("sign", Signature.getSign(params, VeryPayConfig.getAppSecret())); //string(32) 加密验证码

      if (VeryPayConfig.isDebug()) {
        VeryPayConfig.getLog().info(params.toString());
        VeryPayConfig.getLog().info(VeryPayConfig.getApiUrl() + requestUrl);
      }
      params = Utils.MapValueEncoder(params);
      VeryPayConfig.getLog().info(params.toString());
      String result = HttpClientUtil.sendMsgHTTP(params, VeryPayConfig.getApiUrl() + requestUrl);
      setResponseBody(result);
    } catch (Exception e) {
      this.responseBody = this.errResult;
      VeryPayConfig.getLog().error(e, e);
    }
  }

  /**
   * 关闭订单接口
   * 接口说明
   * 以下情况需要调用关单接口：商户订单支付失败需要生成新单号重新发起支付，要对原订单号调用关单，避免重复支付；
   * 系统下单后，用户支付超时，系统退出不再受理，避免用户继续，请调用关单接口。
   * <p/>
   * :!: 特别提示，为了避免商户资金受到损失，已经支付成功的订单无法关闭订单，如果支付成功的订单需要关闭，请走退款流程（请勿在程序里面自动调用退款接口）
   * URL	https://api.pay.verystar.cn/v3/pay/closeorder.json
   * 请求方式	POST
   * 返回格式	JSON
   *
   * @param bean
   * @param requestUrl
   */
  public void closeOrder(CloseOrderRequestBean bean, String requestUrl) {
    try {
      if (!VeryPayConfig.verify())
        return;
      Map<String, Object> params = new HashMap<String, Object>();
      params.put("app_key", VeryPayConfig.getAppKey()); //string(32)	API分配的公钥,和私钥是一对，私钥用于请求加密
      params.put("time", System.currentTimeMillis()); //int 当前请求时间戳(秒)，比如 1494571680
      params.put("store_sn", bean.getStoreSn()); //string(32)	门店编号
      params.put("client_sn", bean.getClientSn()); //string(32)	终端编号
      if (bean.getOrderSn() != null && !"".equals(bean.getOrderSn()))
        params.put("order_sn", bean.getOrderSn()); //string(32)	统一下单接口返回的交易号
      if (bean.getOutSn() != null && !"".equals(bean.getOutSn()))
        params.put("out_sn", bean.getOutSn()); //string(64) 商户订单号，order_sn、out_sn二选一，如果同时存在优先级：order_sn > out_sn
      params.put("sign", Signature.getSign(params, VeryPayConfig.getAppSecret())); //string(32) 加密验证码

      if (VeryPayConfig.isDebug()) {
        VeryPayConfig.getLog().info(params.toString());
        VeryPayConfig.getLog().info(VeryPayConfig.getApiUrl() + requestUrl);
      }
      params = Utils.MapValueEncoder(params);
      VeryPayConfig.getLog().info(params.toString());
      String result = HttpClientUtil.sendMsgHTTP(params, VeryPayConfig.getApiUrl() + requestUrl);
      setResponseBody(result);
    } catch (Exception e) {
      this.responseBody = this.errResult;
      VeryPayConfig.getLog().error(e, e);
    }
  }

  public String getResponseBody() {
    return responseBody;
  }

  public void setResponseBody(String responseBody) {
    if (responseBody != null && !"".equals(responseBody)) {
      VeryPayConfig.getLog().info("API返回的数据如下：");
      VeryPayConfig.getLog().info(responseBody);

      try {
        Map<String, Object> resultMap = GsonUtils.paseJsonToMap(responseBody);
        String serverSign = resultMap.get("sign").toString();

        Map<String, Object> params = GsonUtils.paseJsonToMap(resultMap.get("data").toString());
        if (VeryPayConfig.isDebug()) {
          VeryPayConfig.getLog().info("data:" + params.toString());
        }
        String localSign = Signature.getSign(params, VeryPayConfig.getAppSecret());

        if (!localSign.equals(serverSign)) {
          VeryPayConfig.getLog().info("服务器sign：" + serverSign + ",本地sign:" + localSign);
          VeryPayConfig.getLog().error(">>>>>>>>>>>> 签名错误 <<<<<<<<<<<<");
          responseBody = "{\"retcode\":\"999998\",\"msg\":\"验签失败，详情请咨询\"}";
        }
      } catch (Exception e) {
        responseBody = "{\"retcode\":\"999991\",\"msg\":\"返回结果处理异常,请联系管理人员!\"}";
        VeryPayConfig.getLog().error(e, e);
      }

      this.responseBody = responseBody;
    }
  }
}