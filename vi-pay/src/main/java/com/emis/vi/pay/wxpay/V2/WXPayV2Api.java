package com.emis.vi.pay.wxpay.V2;

import com.emis.vi.pay.util.emisUtil;
import com.emis.vi.pay.wxpay.V2.common.WXPayConfig;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付-API - V2版，支持多账户
 * author: Harry
 * Date: 2017/10/09
 * Time: 上午10:40
 */
public class WXPayV2Api {

  protected Logger oLogger_ = null;
  private WXPayConfig config;

  public Logger getoLogger_() {
    return oLogger_;
  }

  public void setoLogger_(Logger oLogger_) {
    this.oLogger_ = oLogger_;
  }

  public WXPayConfig getConfig() {
    return config;
  }

  public void setConfig(WXPayConfig config) {
    this.config = config;
  }

  public WXPayV2Api() {

  }

  public WXPayV2Api(WXPayConfig config) {
    this.config = config;
  }

  /**
   * 刷卡支付<br>
   * 普通商户: https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_10&index=1
   * 服务商  : https://pay.weixin.qq.com/wiki/doc/api/micropay_sl.php?chapter=9_10&index=1
   *
   * @param data 支付参数
   *             need: body, out_trade_no, total_fee, spbill_create_ip, auth_code
   *             others: sub_appid, sub_mch_id, device_info, detail, attach, goods_tag, limit_pay, scene_info
   * @return 返回的xml转map
   */
  public Map<String, String> doMicroPay(HashMap<String, String> data) {
    if (config == null) {
      return null;
    }
    Map<String, String> res = null;
    try {
      WXPay wxpay = new WXPay(config);
      /*data = new HashMap<String, String>();
      data.put("sub_appid", "");
      data.put("sub_mch_id", "1471011202");
      data.put("device_info", "");
      data.put("body", "群丰 Harry测试_20171009170000_订单");
      data.put("detail", "");
      data.put("attach", "");
      data.put("out_trade_no", "emis-harrytest-20171009170000");
      data.put("total_fee", "1");
      data.put("fee_type", "CNY");
      data.put("spbill_create_ip", "");
      data.put("goods_tag", "");
      data.put("limit_pay", "");
      data.put("auth_code", "***");
      data.put("scene_info", "");*/
      // data.put("time_expire", "20170112104120");
      res = wxpay.microPay(data);
//      System.out.println(res);
      if (oLogger_ != null) {
        oLogger_.info(res);
      }
    } catch (Exception e) {
      e.printStackTrace();
      if (oLogger_ != null) {
        oLogger_.error(e, e);
      }
    }
    return res;
  }

  /**
   * 刷卡支付<br>
   * 普通商户: https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_10&index=1 <br>
   * 服务商  : https://pay.weixin.qq.com/wiki/doc/api/micropay_sl.php?chapter=9_10&index=1
   *
   * @param data 支付参数
   *             need: body, out_trade_no, total_fee, spbill_create_ip, auth_code
   *             others: sub_appid, sub_mch_id, device_info, detail, attach, goods_tag, limit_pay, scene_info
   * @return 返回的xml
   */
  public String doMicroPayRtnXmlData(HashMap<String, String> data) {
    if (config == null) {
      return null;
    }
    String res = null;
    try {
      WXPay wxpay = new WXPay(config);
      res = wxpay.microPayRtnXmlData(data);
      if (oLogger_ != null) {
        oLogger_.info(res);
      }
    } catch (Exception e) {
      e.printStackTrace();
      if (oLogger_ != null) {
        oLogger_.error(e, e);
      }
    }
    return res;
  }


  /**
   * 查询订单 <br>
   * 普通商户: https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_2 <br>
   * 服务商  : https://pay.weixin.qq.com/wiki/doc/api/micropay_sl.php?chapter=9_2
   *
   * @param data 参数
   *             need: transaction_id or out_trade_no
   *             others: sub_appid, sub_mch_id
   * @return 返回的xml转map
   */
  public Map<String, String> doOrderQuery(HashMap<String, String> data) {
    if (config == null) {
      return null;
    }
    Map<String, String> res = null;
    try {
      WXPay wxpay = new WXPay(config);
//      HashMap<String, String> data = new HashMap<String, String>();
//      data.put("transaction_id", "4008852001201608221962061594");
      res = wxpay.orderQuery(data);
//      System.out.println(res);
      if (oLogger_ != null) {
        oLogger_.info(res);
      }
    } catch (Exception e) {
      e.printStackTrace();
      if (oLogger_ != null) {
        oLogger_.error(e, e);
      }
    }
    return res;
  }

  /**
   * 查询订单 <br>
   * 普通商户: https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_2 <br>
   * 服务商  : https://pay.weixin.qq.com/wiki/doc/api/micropay_sl.php?chapter=9_2
   *
   * @param data 参数
   *             need: transaction_id or out_trade_no
   *             others: sub_appid, sub_mch_id
   * @return 返回的xml
   */
  public String doOrderQueryRtnXmlData(HashMap<String, String> data) {
    if (config == null) {
      return null;
    }
    String res = null;
    try {
      WXPay wxpay = new WXPay(config);
      res = wxpay.orderQueryRtnXmlData(data);
      if (oLogger_ != null) {
        oLogger_.info(res);
      }
    } catch (Exception e) {
      e.printStackTrace();
      if (oLogger_ != null) {
        oLogger_.error(e, e);
      }
    }
    return res;
  }

  /**
   * 撤销订单 <br>
   * 普通商户: https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_11&index=3 <br>
   * 服务商  : https://pay.weixin.qq.com/wiki/doc/api/micropay_sl.php?chapter=9_11&index=3
   *
   * @param data 参数
   *             need: transaction_id or out_trade_no
   *             others: sub_appid, sub_mch_id
   * @return 返回的xml转map
   */
  public Map<String, String> doReverse(HashMap<String, String> data) {
    if (config == null) {
      return null;
    }
    Map<String, String> res = null;
    try {
      WXPay wxpay = new WXPay(config);
//      HashMap<String, String> data = new HashMap<String, String>();
//      data.put("transaction_id", "4008852001201608221962061594");
      res = wxpay.reverse(data);
//      System.out.println(res);
      if (oLogger_ != null) {
        oLogger_.info(res);
      }
    } catch (Exception e) {
      e.printStackTrace();
      if (oLogger_ != null) {
        oLogger_.error(e, e);
      }
    }
    return res;
  }

  /**
   * 撤销订单 <br>
   * 普通商户: https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_11&index=3 <br>
   * 服务商  : https://pay.weixin.qq.com/wiki/doc/api/micropay_sl.php?chapter=9_11&index=3
   *
   * @param data 参数
   *             need: transaction_id or out_trade_no
   *             others: sub_appid, sub_mch_id
   * @return 返回的xml
   */
  public String doReverseRtnXmlData(HashMap<String, String> data) {
    if (config == null) {
      return null;
    }
    String res = null;
    try {
      WXPay wxpay = new WXPay(config);
      res = wxpay.reverseRtnXmlData(data);
      if (oLogger_ != null) {
        oLogger_.info(res);
      }
    } catch (Exception e) {
      e.printStackTrace();
      if (oLogger_ != null) {
        oLogger_.error(e, e);
      }
    }
    return res;
  }

  /**
   * 申请退款 <br>
   * 普通商户: https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_4 <br>
   * 服务商  : https://pay.weixin.qq.com/wiki/doc/api/micropay_sl.php?chapter=9_4
   *
   * @param data 参数
   *             need: transaction_id or out_trade_no, out_refund_no, total_fee, refund_fee
   *             others: sub_appid, sub_mch_id, refund_desc, refund_account
   * @return 返回的xml转map
   */
  public Map<String, String> doRefund(HashMap<String, String> data) {
    if (config == null) {
      return null;
    }
    Map<String, String> res = null;
    try {
//    HashMap<String, String> data = new HashMap<String, String>();
//    data.put("out_trade_no", out_trade_no);
//    data.put("out_refund_no", out_trade_no);
//    data.put("total_fee", total_fee);
//    data.put("refund_fee", total_fee);
      if (data.get("refund_fee_type") == null || "".equals(data.get("refund_fee_type"))) {
        data.put("refund_fee_type", "CNY");
      }
      if (data.get("op_user_id") == null || "".equals(data.get("op_user_id"))) {
        data.put("op_user_id", config.getMchID());
      }
      WXPay wxpay = new WXPay(config);
      res = wxpay.refund(data);
//      System.out.println(res);
      if (oLogger_ != null) {
        oLogger_.info(res);
      }
    } catch (Exception e) {
      e.printStackTrace();
      if (oLogger_ != null) {
        oLogger_.error(e, e);
      }
    }
    return res;
  }

  /**
   * 申请退款 <br>
   * 普通商户: https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_4 <br>
   * 服务商  : https://pay.weixin.qq.com/wiki/doc/api/micropay_sl.php?chapter=9_4
   *
   * @param data 参数
   *             need: transaction_id or out_trade_no, out_refund_no, total_fee, refund_fee
   *             others: sub_appid, sub_mch_id, refund_desc, refund_account
   * @return 返回的xml
   */
  public String doRefundRtnXmlData(HashMap<String, String> data) {
    if (config == null) {
      return null;
    }
    String res = null;
    try {
      if (data.get("refund_fee_type") == null || "".equals(data.get("refund_fee_type"))) {
        data.put("refund_fee_type", "CNY");
      }
      if (data.get("op_user_id") == null || "".equals(data.get("op_user_id"))) {
        data.put("op_user_id", config.getMchID());
      }
      WXPay wxpay = new WXPay(config);
      res = wxpay.refundRtnXmlData(data);
      if (oLogger_ != null) {
        oLogger_.info(res);
      }
    } catch (Exception e) {
      e.printStackTrace();
      if (oLogger_ != null) {
        oLogger_.error(e, e);
      }
    }
    return res;
  }

  /**
   * 统一下单 <br>
   * 普通商户: https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1 <br>
   * 服务商  : https://pay.weixin.qq.com/wiki/doc/api/jsapi_sl.php?chapter=9_1
   *
   * @param data 参数
   *             need: body, out_trade_no, total_fee, spbill_create_ip, notify_url, trade_type(公众号支付:JSAPI), openid(公众号支付必填)
   *             others: sub_appid, sub_mch_id, device_info, detail, attach, time_start, time_expire, goods_tag, product_id, limit_pay, sub_openid
   * @return 返回的xml转map
   * 返回重要值: prepay_id, 该值非空表示成功
   */
  public Map<String, String> doUnifiedOrder(HashMap<String, String> data) {
    if (config == null) {
      return null;
    }
    Map<String, String> res = null;
    try {
//      Map<String, String> data = new HashMap<String, String>();
      /*
      data.put("sub_appid", "");
      data.put("sub_mch_id", "1471011202");
      data.put("device_info", "WEB");
      data.put("body", "群丰 Harry测试_20171009170000_订单");
      data.put("detail", "");
      data.put("attach", "");
      data.put("out_trade_no", "emis-harrytest-20171009170000");
      data.put("fee_type", "CNY");
      data.put("total_fee", "1");
      data.put("spbill_create_ip", "127.0.0.1");
      data.put("time_start", "20171013101010");
      data.put("time_expire", "20171013111111");
      data.put("goods_tag", "");
      data.put("notify_url", "http://www.weixin.qq.com/wxpay/pay.php");
      data.put("trade_type", "JSAPI");
      data.put("product_id", "");
      data.put("limit_pay", "");
      data.put("openid", "***");
      data.put("sub_openid", "");
      data.put("scene_info", "");
      */
      WXPay wxpay = new WXPay(config);
      res = wxpay.unifiedOrder(data);
//      System.out.println(res);
      if ("SUCCESS".equalsIgnoreCase(res.get("return_code")) && "SUCCESS".equalsIgnoreCase(res.get("result_code"))) {
        if (oLogger_ != null) {
          oLogger_.info("unifiedOrder.Out_trade_no:" + data.get("out_trade_no") + " -->> Prepay_id:" + res.get("prepay_id"));
        }
      } else {
        res.put("prepay_id", "");
        if (oLogger_ != null) {
          oLogger_.error("unifiedOrder return_msg:" + res.get("return_msg"));
          oLogger_.error("unifiedOrder err_code:" + res.get("err_code"));
          oLogger_.error("unifiedOrder err_code_des:" + res.get("err_code_des"));
        }
      }
      if (oLogger_ != null) {
        oLogger_.info(res);
      }
    } catch (Exception e) {
      e.printStackTrace();
      if (oLogger_ != null) {
        oLogger_.error(e, e);
      }
    }
    return res;
  }

  /**
   * 关闭订单 <br>
   * 普通商户: https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_3 <br>
   * 服务商  : https://pay.weixin.qq.com/wiki/doc/api/jsapi_sl.php?chapter=9_3
   *
   * @param data 参数
   *             need: out_trade_no
   *             others: sub_appid, sub_mch_id
   * @return 返回的xml转map
   */
  public Map<String, String> doCloseOrder(HashMap<String, String> data) {
    if (config == null) {
      return null;
    }
    Map<String, String> res = null;
    try {
      WXPay wxpay = new WXPay(config);
//      HashMap<String, String> data = new HashMap<String, String>();
//      data.put("out_trade_no", "emis-harrytest-20171009170000");
      res = wxpay.closeOrder(data);
//      System.out.println(res);
      if (oLogger_ != null) {
        oLogger_.info(res);
      }
    } catch (Exception e) {
      e.printStackTrace();
      if (oLogger_ != null) {
        oLogger_.error(e, e);
      }
    }
    return res;
  }

  /**
   * 下载对账单 <br>
   * 普通商户: https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_6 <br>
   * 服务商  : https://pay.weixin.qq.com/wiki/doc/api/micropay_sl.php?chapter=9_6
   *
   * @param data 参数
   *             need: bill_date, bill_type
   *             others: sub_appid, sub_mch_id
   * @return 返回的xml转map
   */
  public Map<String, String> downloadBill(HashMap<String, String> data) {
    if (config == null) {
      return null;
    }
    Map<String, String> res = null;
    try {
//      HashMap<String, String> data = new HashMap<String, String>();
//      data.put("bill_date", "20140603");
//      data.put("bill_type", "ALL");
      WXPay wxpay = new WXPay(config);
      res = wxpay.downloadBill(data);
//      System.out.println(res);
      if (oLogger_ != null) {
        oLogger_.info(res);
      }
    } catch (Exception e) {
      e.printStackTrace();
      if (oLogger_ != null) {
        oLogger_.error(e, e);
      }
    }
    return res;
  }

  /**
   * 下载对账单 <br>
   * 普通商户: https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_6 <br>
   * 服务商  : https://pay.weixin.qq.com/wiki/doc/api/micropay_sl.php?chapter=9_6
   *
   * @param data 参数
   *             need: bill_date, bill_type
   *             others: sub_appid, sub_mch_id
   * @return 返回的xml
   */
  public String downloadBillRtnXmlData(HashMap<String, String> data) {
    if (config == null) {
      return null;
    }
    String res = null;
    try {
      WXPay wxpay = new WXPay(config);
      res = wxpay.downloadBillRtnXmlData(data);
      if (oLogger_ != null) {
        oLogger_.info(res);
      }
    } catch (Exception e) {
      e.printStackTrace();
      if (oLogger_ != null) {
        oLogger_.error(e, e);
      }
    }
    return res;
  }

  /**
   * 测试用例1001 <br>
   * https://pay.weixin.qq.com/index.php/extend/open_norecharge/showsandbox?pid=47
   * @param data
   * @return
   */
  public boolean Test1001(HashMap<String, String> data) {
    if (config == null) {
      return false;
    }
    Map<String, String> res = null;
    Map<String, String> resQuery = null;
    String out_trade_no = emisUtil.todayDateAD() + emisUtil.todayTimeS();
    try {
      WXPay wxpay = new WXPay(config, false, true);
//      HashMap<String, String> data = new HashMap<String, String>();
//      data.put("sub_appid", "");
//      data.put("sub_mch_id", "1471011202");
      data.put("device_info", "");
      data.put("body", "1001测试_订单_" + out_trade_no);
      data.put("detail", "");
      data.put("attach", "");
      data.put("out_trade_no", out_trade_no);
      data.put("total_fee", "501");
      data.put("fee_type", "CNY");
      data.put("spbill_create_ip", "");
      data.put("goods_tag", "");
      data.put("limit_pay", "");
      data.put("auth_code", "134601237848091252");
      data.put("scene_info", "");
      // data.put("time_expire", "20170112104120");
      res = wxpay.microPay(data);
      System.out.println(res);
      if (oLogger_ != null) {
        oLogger_.info("microPay");
        oLogger_.info("out_trade_no:" + out_trade_no);
        oLogger_.info(res);
      }

      // 查询一次
      HashMap<String, String> dataQuery = new HashMap<String, String>();
      dataQuery.put("out_trade_no", out_trade_no);
      resQuery = wxpay.orderQuery(dataQuery);
      System.out.println(resQuery);
      if (oLogger_ != null) {
        oLogger_.info("query");
        oLogger_.info(resQuery);
      }

    } catch (Exception e) {
      e.printStackTrace();
      if (oLogger_ != null) {
        oLogger_.error(e, e);
      }
      return false;
    }

    return true;
  }

  public boolean Test1002(HashMap<String, String> data) {
    if (config == null) {
      return false;
    }
    Map<String, String> res = null;
    String out_trade_no = emisUtil.todayDateAD() + emisUtil.todayTimeS();
    try {
      WXPay wxpay = new WXPay(config, false, true);
//      HashMap<String, String> data = new HashMap<String, String>();
//      data.put("sub_appid", "");
//      data.put("sub_mch_id", "1471011202");
      data.put("device_info", "");
      data.put("body", "1002测试_订单_" + out_trade_no);
      data.put("detail", "");
      data.put("attach", "");
      data.put("out_trade_no", out_trade_no);
      data.put("total_fee", "502");
      data.put("fee_type", "CNY");
      data.put("spbill_create_ip", "");
      data.put("goods_tag", "");
      data.put("limit_pay", "");
      data.put("auth_code", "134601237848091252");
      data.put("scene_info", "");
      // data.put("time_expire", "20170112104120");
      res = wxpay.microPay(data);
      System.out.println(res);
      if (oLogger_ != null) {
        oLogger_.info("microPay");
        oLogger_.info("out_trade_no:" + out_trade_no);
        oLogger_.info(res);
      }

      // 查询一次
      Map<String, String> resQuery = null;
      HashMap<String, String> dataQuery = new HashMap<String, String>();
      dataQuery.put("out_trade_no", out_trade_no);
      resQuery = wxpay.orderQuery(dataQuery);
      System.out.println(resQuery);
      if (oLogger_ != null) {
        oLogger_.info("query");
        oLogger_.info(resQuery);
      }

      // 退款一次
      Map<String, String> resRefund = null;
      HashMap<String, String> dataRefund = new HashMap<String, String>();
      dataRefund.put("out_trade_no", out_trade_no);
      dataRefund.put("out_refund_no", out_trade_no);
      dataRefund.put("total_fee", "502");
      dataRefund.put("refund_fee", "502");
      if (dataRefund.get("refund_fee_type") == null || "".equals(dataRefund.get("refund_fee_type"))) {
        dataRefund.put("refund_fee_type", "CNY");
      }
      if (dataRefund.get("op_user_id") == null || "".equals(dataRefund.get("op_user_id"))) {
        dataRefund.put("op_user_id", config.getMchID());
      }
      resRefund = wxpay.refund(dataRefund);
      System.out.println(resRefund);
      if (oLogger_ != null) {
        oLogger_.info("Refund");
        oLogger_.info(resRefund);
      }

      // 查询一次
      Map<String, String> resQueryRefund = null;
      HashMap<String, String> dataQueryRefund = new HashMap<String, String>();
      dataQueryRefund.put("out_trade_no", out_trade_no);
      resQueryRefund = wxpay.refundQuery(dataQueryRefund);
      System.out.println(resQueryRefund);
      if (oLogger_ != null) {
        oLogger_.info("QueryRefund");
        oLogger_.info(resQueryRefund);
      }

    } catch (Exception e) {
      e.printStackTrace();
      if (oLogger_ != null) {
        oLogger_.error(e, e);
      }
      return false;
    }

    return true;
  }

  public boolean Test1005(HashMap<String, String> data) {
    if (config == null) {
      return false;
    }
    Map<String, String> res = null;
    String out_trade_no = emisUtil.todayDateAD() + emisUtil.todayTimeS();
    try {
      WXPay wxpay = new WXPay(config, false, true);
//      HashMap<String, String> data = new HashMap<String, String>();
//      data.put("sub_appid", "");
//      data.put("sub_mch_id", "1471011202");
      data.put("device_info", "");
      data.put("bill_date", emisUtil.todayDateAD());
      data.put("bill_type", "ALL");
//      data.put("tar_type", "GZIP");
      res = wxpay.downloadBill(data);
      System.out.println(res);
      if (oLogger_ != null) {
        oLogger_.info("microPay");
        oLogger_.info("out_trade_no:" + out_trade_no);
        oLogger_.info(res);
      }
    } catch (Exception e) {
      e.printStackTrace();
      if (oLogger_ != null) {
        oLogger_.error(e, e);
      }
      return false;
    }

    return true;
  }

}