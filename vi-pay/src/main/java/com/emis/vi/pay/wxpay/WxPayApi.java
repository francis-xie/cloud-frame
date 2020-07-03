package com.emis.vi.pay.wxpay;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.apache.log4j.Logger;

import java.io.Writer;

/**
 * 微信支付-API
 * author: Harry
 * Date: 2016/06/22
 * Time: 上午10:40
 */
public class WxPayApi {

  protected Logger oLogger_ = null;

  public Logger getoLogger_() {
    return oLogger_;
  }

  public void setoLogger_(Logger oLogger_) {
    this.oLogger_ = oLogger_;
  }

  /**
   * 统一下单
   * 除被扫支付场景以外，商户系统先调用该接口在微信支付服务后台生成预支付交易单，返回正确的预支付交易回话标识后再按扫码、JSAPI、APP等不同场景生成交易串调起支付。
   * @param payInfo  统一下单参数
   * @return 成功返回 prepay_id 失败返回空
   */
  public String unifiedOrder(WxData_UnifiedOrder payInfo) {
    String sResult = "";

    // 统一下单 接口连接
    String unifiedorder_URL= "https://api.mch.weixin.qq.com/pay/unifiedorder";

    StringBuffer payInfoXML = new StringBuffer();
    try {
      // 1. 产生xml格式
      xstream.alias("xml", payInfo.getClass());
      payInfoXML.append(xstream.toXML(payInfo));
//      System.out.println(payInfoXML.toString());

      // 2. 发送
      String unifiedOrderResXML = WxPayUtils.httpPost(unifiedorder_URL, payInfoXML.toString());
      System.out.println(unifiedOrderResXML);

      //将从API返回的XML数据映射到Java对象
      WxData_UnifiedOrderRes unifiedOrderRes = (WxData_UnifiedOrderRes) WxPayUtils.fromXML(unifiedOrderResXML, WxData_UnifiedOrderRes.class);
//      sResult = unifiedOrderRes.getPrepay_id();
      if ("SUCCESS".equalsIgnoreCase(unifiedOrderRes.getReturn_code()) && "SUCCESS".equalsIgnoreCase(unifiedOrderRes.getResult_code())) {
        sResult = unifiedOrderRes.getPrepay_id();
        if(oLogger_ != null) {
          oLogger_.info("unifiedOrder.Out_trade_no:" + payInfo.getOut_trade_no() + " -->> Prepay_id:" + sResult);
        }

      } else {
        if(oLogger_ != null) {
          oLogger_.error("unifiedOrder return_msg:" + unifiedOrderRes.getReturn_msg());
          oLogger_.error("unifiedOrder err_code:" + unifiedOrderRes.getErr_code());
          oLogger_.error("unifiedOrder err_code_des:" + unifiedOrderRes.getErr_code_des());
        }
      }
//      System.out.println("订单详情扩展字符串:" + unifiedOrderRes.getPrepay_id());
    } catch (Exception ex) {
      ex.printStackTrace();
      if(oLogger_ != null) {
        oLogger_.error(ex, ex);
      }
    } finally {
      payInfoXML.setLength(0);
      payInfoXML = null;
    }

    return sResult;
  }

  /**
   * 扩展xstream使其支持CDATA
   */
  private static XStream xstream = new XStream(new XppDriver(new NoNameCoder()) {
    public HierarchicalStreamWriter createWriter(Writer out) {
      return new PrettyPrintWriter(out) {
        //增加CDATA标记
        boolean cdata = true;

        @Override
        @SuppressWarnings("rawtypes")
        public void startNode(String name, Class clazz) {
          super.startNode(name, clazz);
        }

        @Override
        public String encodeNode(String name) {
          return name;
        }

        protected void writeText(QuickWriter writer, String text) {
          if (cdata) {
            writer.write("<![CDATA[");
            writer.write(text);
            writer.write("]]>");
          } else {
            writer.write(text);
          }
        }
      };
    }
  });

  /**
   * 统一订单参数转XML
   * @param pi  统一订单参数类
   * @return xml格式字符串
   */
  private static String unifiedOrder2XML(WxData_UnifiedOrder pi) {
    xstream.alias("xml", pi.getClass());
    return xstream.toXML(pi);
  }

  /**
   * 查询订单
   * 该接口提供所有微信支付订单的查询，商户可以通过该接口主动查询订单状态，完成下一步的业务逻辑。
   需要调用查询接口的情况：
   ◆ 当商户后台、网络、服务器等出现异常，商户系统最终未接收到支付通知；
   ◆ 调用支付接口后，返回系统错误或未知交易状态情况；
   ◆ 调用被扫支付API，返回USERPAYING的状态；
   ◆ 调用关单或撤销接口API之前，需确认支付状态；
   * @param orderInfo  查询订单参数
   * @return 成功返回 [交易状态] 失败返回空
   */
  public WxData_OrderQueryRes orderQuery(WxData_OrderQuery orderInfo) {
    // String sResult = "";
    WxData_OrderQueryRes orderQueryRes = null;

    // 查询订单 接口连接
    String orderquery_URL= "https://api.mch.weixin.qq.com/pay/orderquery";

    StringBuffer orderInfoXML = new StringBuffer();
    try {
      // 1. 产生xml格式
      xstream.alias("xml", orderInfo.getClass());
      orderInfoXML.append(xstream.toXML(orderInfo));
//      System.out.println(payInfoXML.toString());

      // 2. 发送
      String orderQueryResXML = WxPayUtils.httpPost(orderquery_URL, orderInfoXML.toString());
      System.out.println(orderQueryResXML);

      //将从API返回的XML数据映射到Java对象
      orderQueryRes = (WxData_OrderQueryRes) WxPayUtils.fromXML(orderQueryResXML, WxData_OrderQueryRes.class);
      /*if ("SUCCESS".equalsIgnoreCase(orderQueryRes.getReturn_code()) && "SUCCESS".equalsIgnoreCase(orderQueryRes.getResult_code())) {
        sResult = orderQueryRes.getTrade_state();
      } else {
        if(oLogger_ != null) {
          oLogger_.error("unifiedOrder return_msg:" + orderQueryRes.getReturn_msg());
          oLogger_.error("unifiedOrder err_code:" + orderQueryRes.getErr_code());
          oLogger_.error("unifiedOrder err_code_des:" + orderQueryRes.getErr_code_des());
        }
      }*/
    } catch (Exception ex) {
      ex.printStackTrace();
      if(oLogger_ != null) {
        oLogger_.error(ex, ex);
      }
    } finally {
      orderInfoXML.setLength(0);
      orderInfoXML = null;
    }

    return orderQueryRes;
  }

  /**
   * 关闭订单
   * 以下情况需要调用关单接口：商户订单支付失败需要生成新单号重新发起支付，要对原订单号调用关单，避免重复支付；系统下单后，用户支付超时，系统退出不再受理，避免用户继续，请调用关单接口。
   注意：订单生成后不能马上调用关单接口，最短调用时间间隔为5分钟。
   * @param orderInfo  关闭订单参数
   * @return 成功返回 OK 失败返回空
   */
  public String closeOrder(WxData_CloseOrder orderInfo) {
    String sResult = "";

    // 查询订单 接口连接
    String orderquery_URL= "https://api.mch.weixin.qq.com/pay/closeorder";

    StringBuffer orderInfoXML = new StringBuffer();
    try {
      // 1. 产生xml格式
      xstream.alias("xml", orderInfo.getClass());
      orderInfoXML.append(xstream.toXML(orderInfo));
//      System.out.println(payInfoXML.toString());

      // 2. 发送
      String orderQueryResXML = WxPayUtils.httpPost(orderquery_URL, orderInfoXML.toString());
      System.out.println(orderQueryResXML);

      //将从API返回的XML数据映射到Java对象
      WxData_CloseOrderRes orderQueryRes = (WxData_CloseOrderRes) WxPayUtils.fromXML(orderQueryResXML, WxData_CloseOrderRes.class);
      if ("SUCCESS".equalsIgnoreCase(orderQueryRes.getReturn_code()) && (orderQueryRes.getErr_code() == null || "".equalsIgnoreCase(orderQueryRes.getErr_code())) ) {
        sResult = "OK";
      } else {
        if(oLogger_ != null) {
          oLogger_.error("unifiedOrder return_msg:" + orderQueryRes.getReturn_msg());
          oLogger_.error("unifiedOrder err_code:" + orderQueryRes.getErr_code());
          oLogger_.error("unifiedOrder err_code_des:" + orderQueryRes.getErr_code_des());
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      if(oLogger_ != null) {
        oLogger_.error(ex, ex);
      }
    } finally {
      orderInfoXML.setLength(0);
      orderInfoXML = null;
    }

    return sResult;
  }

  /**
   * 申请退款
   * 当交易发生之后一段时间内，由于买家或者卖家的原因需要退款时，卖家可以通过退款接口将支付款退还给买家，微信支付将在收到退款请求并且验证成功之后，按照退款规则将支付款按原路退到买家帐号上。
   注意：
   1、交易时间超过一年的订单无法提交退款；
   2、微信支付退款支持单笔交易分多次退款，多次退款需要提交原支付订单的商户订单号和设置不同的退款单号。一笔退款失败后重新提交，要采用原来的退款单号。总退款金额不能超过用户实际支付金额。
   * @param orderInfo  关闭订单参数
   * @return 成功返回 OK 失败返回空
   * 此接口需要 证书认证， 请使用WxScanPayBusiness.doRefund 来实现
   */
/*
  public String refund(WxData_Refund orderInfo) {
    String sResult = "";

    // 查询订单 接口连接
    String orderquery_URL= "https://api.mch.weixin.qq.com/secapi/pay/refund";

    StringBuffer orderInfoXML = new StringBuffer();
    try {
      // 1. 产生xml格式
      xstream.alias("xml", orderInfo.getClass());
      orderInfoXML.append(xstream.toXML(orderInfo));
//      System.out.println(payInfoXML.toString());

      // 2. 发送
      String orderQueryResXML = WxPayUtils.httpPost(orderquery_URL, orderInfoXML.toString());
      System.out.println(orderQueryResXML);

      //将从API返回的XML数据映射到Java对象
      WxData_RefundRes orderQueryRes = (WxData_RefundRes) WxPayUtils.fromXML(orderQueryResXML, WxData_RefundRes.class);
      if ("SUCCESS".equalsIgnoreCase(orderQueryRes.getReturn_code()) && (orderQueryRes.getErr_code() == null || "".equalsIgnoreCase(orderQueryRes.getErr_code())) ) {
        sResult = "OK";
      } else {
        if(oLogger_ != null) {
          oLogger_.error("unifiedOrder return_msg:" + orderQueryRes.getReturn_msg());
          oLogger_.error("unifiedOrder err_code:" + orderQueryRes.getErr_code());
          oLogger_.error("unifiedOrder err_code_des:" + orderQueryRes.getErr_code_des());
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      if(oLogger_ != null) {
        oLogger_.error(ex, ex);
      }
    } finally {
      orderInfoXML.setLength(0);
      orderInfoXML = null;
    }

    return sResult;
  }
*/



  /**
   * 查询退款
   * 提交退款申请后，通过调用该接口查询退款状态。退款有一定延时，用零钱支付的退款20分钟内到账，银行卡支付的退款3个工作日后重新查询退款状态。
   * @param orderInfo  关闭订单参数
   * @return 成功返回 OK 失败返回空
   */
  public String refundQuery(WxData_RefundQuery orderInfo) {
    String sResult = "";

    // 查询订单 接口连接
    String orderquery_URL= "https://api.mch.weixin.qq.com/pay/refundquery";

    StringBuffer orderInfoXML = new StringBuffer();
    try {
      // 1. 产生xml格式
      xstream.alias("xml", orderInfo.getClass());
      orderInfoXML.append(xstream.toXML(orderInfo));
//      System.out.println(payInfoXML.toString());

      // 2. 发送
      String orderQueryResXML = WxPayUtils.httpPost(orderquery_URL, orderInfoXML.toString());
      System.out.println(orderQueryResXML);

      //将从API返回的XML数据映射到Java对象
      WxData_RefundQueryRes orderQueryRes = (WxData_RefundQueryRes) WxPayUtils.fromXML(orderQueryResXML, WxData_RefundQueryRes.class);
      if ("SUCCESS".equalsIgnoreCase(orderQueryRes.getReturn_code()) && "SUCCESS".equalsIgnoreCase(orderQueryRes.getResult_code()) ) {
        sResult = "OK";
      } else {
        if(oLogger_ != null) {
          oLogger_.error("unifiedOrder return_msg:" + orderQueryRes.getReturn_msg());
          oLogger_.error("unifiedOrder err_code:" + orderQueryRes.getErr_code());
          oLogger_.error("unifiedOrder err_code_des:" + orderQueryRes.getErr_code_des());
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      if(oLogger_ != null) {
        oLogger_.error(ex, ex);
      }
    } finally {
      orderInfoXML.setLength(0);
      orderInfoXML = null;
    }

    return sResult;
  }


}
