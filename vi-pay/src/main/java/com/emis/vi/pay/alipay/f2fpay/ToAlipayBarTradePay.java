  /**
 * $Id: ToAlipayBarTradePay.java 15226 2015-09-14 01:15:17Z francis.xie $
 * 2015/06/17 Francis.xie Modify 需求 #30229 [标准版-Venus] 支付宝升级1.0升到2.0
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.emis.vi.pay.alipay.f2fpay;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.TradeFundBill;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.emis.vi.pay.alipay.constants.AlipayServiceEnvConstants;
import com.emis.vi.pay.alipay.factory.AlipayAPIClientFactory;

public class ToAlipayBarTradePay {

  private String amount = "";
  private String fund_channel = "";
  private String responseBody = "";

  public static void main(String[] args) {
    //201504210011041195
    String out_trade_no = "20150302201432234"; // 商户唯一订单号
    String auth_code = "201504238812381043"; // 扫码枪扫描到的用户手机钱包中的付款条码

    //barPay(out_trade_no,auth_code);
    //cancelOrder(out_trade_no);

    String trade_no = "2015050521001004720200031381";
    String refund_amount = "0.01";
//    refundOrder(trade_no, refund_amount, "");
  }

  /**
   * 条码下单支付
   *
   * @param out_trade_no 商户订单号
   * @param auth_code    支付授权码
   * @param total_amount 订单总金额
   * @param subject      订单标题
   * @param seller_id    卖家支付宝用户号
   * @param goods_detail    商品明细列表
   * @param operator_id  商户操作员编号
   * @param store_id     商户门店编号
   * @param terminal_id  机具终端编号
   */
  public void barPay(String out_trade_no, String auth_code, String total_amount, String subject, String seller_id,
                     String goods_detail, String operator_id, String store_id, String terminal_id,String alipay_store_id) {
    AlipayServiceEnvConstants.logger.info("barPay>>>Start");

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String time_expire = sdf.format(System.currentTimeMillis() + 1 * 6 * 60 * 1000);  //支付超时时间 6分钟

    StringBuilder sb = new StringBuilder();
    sb.append("{\"out_trade_no\":\"" + out_trade_no + "\","); //商户订单号
    sb.append("\"scene\":\"bar_code\","); //支付场景，条码支付，取值:bar_code
    sb.append("\"auth_code\":\"" + auth_code + "\",");  //支付授权码
    sb.append("\"seller_id\":\"" + seller_id + "\",");  //卖家支付宝用户 ID
    sb.append("\"total_amount\":\"" + total_amount + "\",");  //订单总金额
    sb.append("\"subject\":\"" + subject + "\",");  //订单标题
    if (goods_detail != null && !"".equals(goods_detail)) {
      sb.append("\"goods_detail\":" + goods_detail + ",");  //商品明细列表
    } else {
      sb.append("\"goods_detail\":[{" + //商品明细列表
        "\"goods_id\":\"" + AlipayServiceEnvConstants.goods_id + "\"," +  //商品编号
        "\"goods_name\":\"" + AlipayServiceEnvConstants.goods_name + "\"," +  //商品名称
        "\"quantity\":\"1\"," + //商品数量
        "\"price\":\"" + total_amount + "\"}],"); //商品单价
    }
    sb.append("\"operator_id\":\"" + operator_id + "\",");  //商户操作员编号
    sb.append("\"store_id\":\"" + store_id + "\",");  //商户门店编号
    sb.append("\"terminal_id\":\"" + terminal_id + "\",");  //机具终端编号
    if(alipay_store_id!=null&&!"".equals(alipay_store_id)){
      sb.append("\"alipay_store_id\":\"" + alipay_store_id + "\",");//支付宝的店铺编号
    }
    sb.append("\"extend_params\":{\"sys_service_provider_id\":\"" + AlipayServiceEnvConstants.sys_service_provider_id + "\"},"); //商品单价
    sb.append("\"time_expire\":\"" + time_expire + "\"}");  //支付超时时间，该笔订单允许的最晚付款时间，逾期将关闭交易

    AlipayServiceEnvConstants.logger.info("barPay>>>" + sb.toString());

    AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient();
    // 使用SDK，构建群发请求模型
    AlipayTradePayRequest request = new AlipayTradePayRequest();
    request.setBizContent(sb.toString());

    AlipayTradePayResponse response = null;
    try {
      // 使用SDK，调用交易下单接口
      response = alipayClient.execute(request);

      AlipayServiceEnvConstants.logger.info("barPay>>>getBody>>>" + response.getBody());
      AlipayServiceEnvConstants.logger.info("barPay>>>isSuccess>>>" + response.isSuccess());
      AlipayServiceEnvConstants.logger.info("barPay>>>结果码>>>" + response.getCode());
      AlipayServiceEnvConstants.logger.info("barPay>>>结果码描述>>>" + response.getMsg());
      AlipayServiceEnvConstants.logger.info("barPay>>>商户订单号>>>" + response.getOutTradeNo());
      AlipayServiceEnvConstants.logger.info("barPay>>>支付宝交易号>>>" + response.getTradeNo());
      AlipayServiceEnvConstants.logger.info("barPay>>>买家账号>>>" + response.getBuyerLogonId());

      // 这里只是简单的打印，请开发者根据实际情况自行进行处理
      if (null != response && response.isSuccess()) {
        if (response.getCode().equals("10000")) {
          AlipayServiceEnvConstants.logger.info("barPay>>>付款金额>>>" + response.getBuyerPayAmount());
          AlipayServiceEnvConstants.logger.info("barPay>>>实收金额>>>" + response.getReceiptAmount());
          AlipayServiceEnvConstants.logger.info("barPay>>>开票金额>>>" + response.getInvoiceAmount());
          AlipayServiceEnvConstants.logger.info("barPay>>>付款openid>>>" + response.getOpenId());
          AlipayServiceEnvConstants.logger.info("barPay>>>总金额>>>" + response.getTotalAmount());
          AlipayServiceEnvConstants.logger.info("barPay>>>支付时间>>>" + response.getGmtPayment());

          List<TradeFundBill> fund_bill_list = response.getFundBillList();
          if (null != fund_bill_list) {
            doFundBillList(fund_bill_list);

            AlipayServiceEnvConstants.logger.info("barPay>>>付款资金>>>" + amount);
            AlipayServiceEnvConstants.logger.info("barPay>>>付款资金渠道>>>" + fund_channel);
          }
        } else if (response.getCode().equals("10003")) {
          // 对于返回付款中状态，需要调用收单查询接口查询订单付款状态
          // queryRetry(out_trade_no);
        }
      } else {
        // 打印错误码
        AlipayServiceEnvConstants.logger.info("barPay>>>getSubCode>>>" + response.getSubCode());
        AlipayServiceEnvConstants.logger.info("barPay>>>getSubMsg>>>" + response.getSubMsg());
      }

      if (null != response) {
        setResponseMsg("1", response.isSuccess(), response.getCode(), response.getOutTradeNo(), response.getTradeNo(),
          response.getBuyerLogonId(), response.getBody(), "", response.getBuyerPayAmount(), response.getTotalAmount(),
          response.getReceiptAmount(), response.getInvoiceAmount());
      }

    } catch (AlipayApiException e) {
      AlipayServiceEnvConstants.logger.info("barPay>>>" + e.getMessage());
    }

    AlipayServiceEnvConstants.logger.info("barPay>>>End");
  }

  /**
   * 资金明细信息说明
   *
   * @param fund_bill_list
   */
  public void doFundBillList(List<TradeFundBill> fund_bill_list) {
    // 根据付款的资金渠道，来决定哪些是商户优惠，哪些是支付宝优惠。 对账时要注意商户优惠部分
    for (TradeFundBill tfb : fund_bill_list) {
      amount += tfb.getAmount() + "/";
      fund_channel += tfb.getFundChannel() + "/";
    }
    if (!"".equals(amount)) {
      amount = amount.substring(0, amount.length() - 1);
    }
    if (!"".equals(fund_channel)) {
      fund_channel = fund_channel.substring(0, fund_channel.length() - 1);
    }
  }

  /**
   * 交易查询
   *
   * @param out_trade_no 商户订单号
   * @return
   */
  public AlipayTradeQueryResponse query(final String out_trade_no) {
    AlipayServiceEnvConstants.logger.info("query>>>Start");

    AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient();
    AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();

    String biz_content = "{\"out_trade_no\":\"" + out_trade_no + "\"}";
    request.setBizContent(biz_content);

    AlipayTradeQueryResponse response = null;
    try {
      response = alipayClient.execute(request);
      AlipayServiceEnvConstants.logger.info("query>>>getBody>>>" + response.getBody());
      AlipayServiceEnvConstants.logger.info("query>>>isSuccess>>>" + response.isSuccess());
      AlipayServiceEnvConstants.logger.info("query>>>结果码>>>" + response.getCode());
      AlipayServiceEnvConstants.logger.info("query>>>结果码描述>>>" + response.getMsg());

      if (null != response && response.isSuccess()) {
        AlipayServiceEnvConstants.logger.info("barPay>>>买家账号：" + response.getBuyerLogonId());
        AlipayServiceEnvConstants.logger.info("barPay>>>商户订单号：" + response.getOutTradeNo());
        AlipayServiceEnvConstants.logger.info("barPay>>>支付宝交易号：" + response.getTradeNo());
        AlipayServiceEnvConstants.logger.info("barPay>>>总金额：" + response.getTotalAmount());
        AlipayServiceEnvConstants.logger.info("barPay>>>订单状态：" + response.getTradeStatus());

        if (response.getCode().equals("10000")) {
          if ("TRADE_SUCCESS".equalsIgnoreCase(response.getTradeStatus())) {
            List<TradeFundBill> fund_bill_list = response.getFundBillList();
            if (null != fund_bill_list) {
              doFundBillList(fund_bill_list);
            }
          } else if ("WAIT_BUYER_PAY".equalsIgnoreCase(response.getTradeStatus())) {
            // 等待用户付款状态，需要轮询查询用户的付款结果
            // queryRetry(out_trade_no);
          } else if ("TRADE_CLOSED".equalsIgnoreCase(response.getTradeStatus())) {
            // 表示未付款关闭，或已付款的订单全额退款后关闭
          } else if ("TRADE_FINISHED".equalsIgnoreCase(response.getTradeStatus())) {
            // 此状态，订单不可退款或撤销
          }
        } else {
          // 如果请求未成功，请重试
        }

        setResponseMsg("2", response.isSuccess(), response.getCode(), response.getOutTradeNo(), response.getTradeNo(),
          response.getBuyerLogonId(), response.getBody(), response.getTradeStatus(), response.getBuyerPayAmount(),
          response.getTotalAmount(), response.getReceiptAmount(), response.getInvoiceAmount());
      }
    } catch (AlipayApiException e) {
      AlipayServiceEnvConstants.logger.error("query>>>" + e.getMessage());
    }

    AlipayServiceEnvConstants.logger.info("query>>>End");
    return response;
  }

  /**
   * 轮询查询订单状态
   *
   * @param out_trade_no 商户订单号
   */
  public void queryRetry(final String out_trade_no) {
    AlipayServiceEnvConstants.logger.info("queryRetry>>>Start");

    final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    final int queryTime = 60;// 总共轮询查询时间，单位秒
    final int queryPeriod = 5;// 间隔时间，单位秒

    Runnable queryRunnable = new Runnable() {
      int i = 0;
      int n = queryTime / queryPeriod;

      public void run() {
        if (++i <= n) {
          AlipayServiceEnvConstants.logger.info("queryRetry>>>重试查询第 " + i + " 次");

          AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient();
          AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
          String biz_content = "{\"out_trade_no\":\"" + out_trade_no + "\"}";
          request.setBizContent(biz_content);

          try {
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            if (null != response && response.isSuccess()) {
              AlipayServiceEnvConstants.logger.info("queryRetry>>>付款金额>>>" + response.getBuyerPayAmount());
              AlipayServiceEnvConstants.logger.info("queryRetry>>>付款openid>>>" + response.getOpenId());
              AlipayServiceEnvConstants.logger.info("queryRetry>>>总金额>>>" + response.getTotalAmount());
              AlipayServiceEnvConstants.logger.info("queryRetry>>>订单状态>>>" + response.getTradeStatus());

              if (response.getCode().equals("10000") && "TRADE_SUCCESS".equalsIgnoreCase(response.getTradeStatus())) {
                // 查询到付款成功，处理业务入账，退出轮询查询
                // 业务入账，通知收银员出货

                List<TradeFundBill> fund_bill_list = response.getFundBillList();
                if (null != fund_bill_list) {
                  doFundBillList(fund_bill_list);
                }
                setResponseMsg("2", response.isSuccess(), response.getCode(), response.getOutTradeNo(), response.getTradeNo(),
                  response.getBuyerLogonId(), response.getBody(), response.getTradeStatus(), response.getBuyerPayAmount(),
                  response.getTotalAmount(), response.getReceiptAmount(), response.getInvoiceAmount());
                // 收款成功，退出轮询
                AlipayServiceEnvConstants.logger.info("queryRetry>>>End");
                service.shutdownNow();
              }
            }

            if (i == n) {
              // 最后一次查询时，仍然没有查询到付款成功，需要撤销订单
              cancelOrder(out_trade_no);

              //退出轮询
              AlipayServiceEnvConstants.logger.info("queryRetry>>>End");
              service.shutdownNow();
            }
          } catch (AlipayApiException e) {
            AlipayServiceEnvConstants.logger.error("queryRetry>>>" + e.getMessage());
          }

        }
      }
    };

    service.scheduleAtFixedRate(queryRunnable, 0, queryPeriod, TimeUnit.SECONDS);
  }

  /**
   * 撤销订单
   *
   * @param out_trade_no 商户订单号
   * @return
   */
  public AlipayTradeCancelResponse cancelOrder(final String out_trade_no) {
    AlipayServiceEnvConstants.logger.info("cancelOrder>>>Start");

    AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient();
    AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();

    String biz_content = "{\"out_trade_no\":\"" + out_trade_no + "\"}";
    request.setBizContent(biz_content);

    AlipayTradeCancelResponse response = null;
    try {
      response = alipayClient.execute(request);

      AlipayServiceEnvConstants.logger.info("cancelOrder>>>getBody>>>" + response.getBody());
      AlipayServiceEnvConstants.logger.info("cancelOrder>>>isSuccess>>>" + response.isSuccess());
      AlipayServiceEnvConstants.logger.info("cancelOrder>>>getMsg>>>" + response.getMsg());
      AlipayServiceEnvConstants.logger.info("cancelOrder>>>getAction>>>" + response.getAction());
      AlipayServiceEnvConstants.logger.info("cancelOrder>>>getCode>>>" + response.getCode());
      AlipayServiceEnvConstants.logger.info("cancelOrder>>>getOutTradeNo>>>" + response.getOutTradeNo());
      AlipayServiceEnvConstants.logger.info("cancelOrder>>>getRetryFlag>>>" + response.getRetryFlag());
      AlipayServiceEnvConstants.logger.info("cancelOrder>>>getSubCode>>>" + response.getSubCode());
      AlipayServiceEnvConstants.logger.info("cancelOrder>>>getSubMsg>>>" + response.getSubMsg());
      AlipayServiceEnvConstants.logger.info("cancelOrder>>>getTradeNo>>>" + response.getTradeNo());

      if (null != response && response.isSuccess()) {
        if (response.getCode().equals("10000")) {

        } else {
          // 没有撤销成功，需要重试几次
          if (response.getRetryFlag().equals("Y")) {
            // 如果重试标识为Y，表示支付宝撤销失败，需要轮询重新发起撤销
            cancelOrderRetry(out_trade_no);
          }
        }
      }

      if (null != response) {
        setResponseMsg("3", response.isSuccess(), response.getCode(), response.getOutTradeNo(),
          response.getTradeNo(), "", response.getBody(), "", "", "", "", "");
      }
    } catch (AlipayApiException e) {
      AlipayServiceEnvConstants.logger.info("cancelOrder>>>" + e.getMessage());
    }

    AlipayServiceEnvConstants.logger.info("cancelOrder>>>End");
    return response;
  }

  /**
   * 轮询发起撤销重试
   *
   * @param out_trade_no 商户订单号
   */
  public void cancelOrderRetry(final String out_trade_no) {
    AlipayServiceEnvConstants.logger.info("cancelOrderRetry>>>Start");

    final AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient();
    final AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();

    String biz_content = "{\"out_trade_no\":\"" + out_trade_no + "\"}";
    request.setBizContent(biz_content);

    // 子线程异步方式，每个5秒钟重试一次，重试2次,加上重试前的1次，总共3次15秒
    new Thread(new Runnable() {
      int i = 0;
      int n = 2;

      public void run() {
        while (++i <= n) {
          try {
            Thread.sleep(5000);
            AlipayServiceEnvConstants.logger.info("cancelOrderRetry>>>重试撤销请求 第 " + i + " 次");
            AlipayTradeCancelResponse response = alipayClient.execute(request);

            AlipayServiceEnvConstants.logger.info("cancelOrderRetry>>>getBody>>>" + response.getBody());
            AlipayServiceEnvConstants.logger.info("cancelOrderRetry>>>isSuccess>>>" + response.isSuccess());
            AlipayServiceEnvConstants.logger.info("cancelOrderRetry>>>getMsg>>>" + response.getMsg());

            if (null != response && response.isSuccess()) {
              if (response.getCode().equals("10000") && response.getBody().contains("\"retry_flag\":\"N\"")) {
                setResponseMsg("3", response.isSuccess(), response.getCode(), response.getOutTradeNo(),
                  response.getTradeNo(), "", response.getBody(), "", "", "", "", "");
                break;
              }
            }

            if (i == n) {
              // 处理到最后一次，还是未撤销成功，需要在商户数据库中对此单最标记，人工介入处理
              setResponseMsg("3", response.isSuccess(), response.getCode(), response.getOutTradeNo(),
                response.getTradeNo(), "", response.getBody(), "", "", "", "", "");
            }

          } catch (AlipayApiException e) {
            AlipayServiceEnvConstants.logger.info("cancelOrder>>>" + e.getMessage());
          } catch (InterruptedException e) {
            AlipayServiceEnvConstants.logger.info("cancelOrder>>>" + e.getMessage());
          }
        }
        AlipayServiceEnvConstants.logger.info("cancelOrder>>>End");
      }
    }).start();

  }

  /**
   * 申请退款
   *
   * @param trade_no       支付宝交易号
   * @param refund_amount  退款金额
   * @param out_request_no 商户退款请求号
   * @param store_id       商户的门店编号
   * @param terminal_id    商户的终端编号
   * @return
   */
  public AlipayTradeRefundResponse refundOrder(String trade_no, String refund_amount, String out_request_no,
                                               String store_id, String terminal_id) {
    AlipayServiceEnvConstants.logger.info("refundOrder>>>Start");

    AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient();
    AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();

    String biz_content = "{\"trade_no\":\"" + trade_no + "\"," +  //支付宝交易号
      "\"refund_amount\":\"" + refund_amount + "\"," +  //退款金额
      "\"out_request_no\":\"" + out_request_no + "\"," +  //标识一次退款请求，同一笔交易多次退款需要保证唯一
      "\"store_id\":\"" + store_id + "\"," +  //商户的门店编号
      "\"terminal_id\":\"" + terminal_id + "\"}"; //商户的终端编号

    AlipayServiceEnvConstants.logger.info("refundOrder>>>" + biz_content);
    request.setBizContent(biz_content);

    AlipayTradeRefundResponse response = null;

    try {
      response = alipayClient.execute(request);
      AlipayServiceEnvConstants.logger.info("refundOrder>>>getBody>>>" + response.getBody());
      AlipayServiceEnvConstants.logger.info("refundOrder>>>getCode>>>" + response.getCode());
      AlipayServiceEnvConstants.logger.info("refundOrder>>>getMsg>>>" + response.getMsg());
      AlipayServiceEnvConstants.logger.info("refundOrder>>>getSubCode>>>" + response.getSubCode());
      AlipayServiceEnvConstants.logger.info("refundOrder>>>getSubMsg>>>" + response.getSubMsg());
      AlipayServiceEnvConstants.logger.info("refundOrder>>>getBuyerLogonId>>>" + response.getBuyerLogonId());
      AlipayServiceEnvConstants.logger.info("refundOrder>>>getFundChange>>>" + response.getFundChange());
      AlipayServiceEnvConstants.logger.info("refundOrder>>>getOpenId>>>" + response.getOpenId());
      AlipayServiceEnvConstants.logger.info("refundOrder>>>getOutTradeNo>>>" + response.getOutTradeNo());
      AlipayServiceEnvConstants.logger.info("refundOrder>>>getRefundFee>>>" + response.getRefundFee());
      AlipayServiceEnvConstants.logger.info("refundOrder>>>getGmtRefundPay>>>" + response.getGmtRefundPay());
      AlipayServiceEnvConstants.logger.info("refundOrder>>>getOpenId>>>" + response.getOpenId());

      if (null != response && response.isSuccess()) {
        if (response.getCode().equals("10000")) {
          if (response.getFundChange().equals("Y")) {
            // 退款成功,资金有变动,做业务及账务处理
          } else {
            //资金无变动，不必做账务处理

          }
        } else {
          // 没有撤销成功，需要重试几次
          refundOrderRetry(trade_no, refund_amount, out_request_no, 2, store_id, terminal_id);
        }
      }

      if (null != response) {
        setResponseMsg("4", response.isSuccess(), response.getCode(), response.getOutTradeNo(),
          response.getTradeNo(), response.getBuyerLogonId(), response.getBody(), "", "", "", "", "");
      }
    } catch (AlipayApiException e) {
      AlipayServiceEnvConstants.logger.info("refundOrder>>>" + e.getMessage());
    }

    AlipayServiceEnvConstants.logger.info("refundOrder>>>End");
    return response;
  }

  /**
   * 同一个out_request_no代表是对同一次退款进行重试处理，不同的out_request_no表示再次发起了退款请求，（部分退款时请谨慎）
   *
   * @param trade_no       支付宝交易号
   * @param refund_amount  退款金额
   * @param out_request_no 商户退款请求号
   * @param retryTimes
   * @param store_id       商户的门店编号
   * @param terminal_id    商户的终端编号
   */
  public void refundOrderRetry(String trade_no, String refund_amount, String out_request_no,
                               int retryTimes, String store_id, String terminal_id) {
    AlipayServiceEnvConstants.logger.info("refundOrderRetry>>>Start");

    AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient();
    AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
    String biz_content = "{\"trade_no\":\"" + trade_no + "\"," +
      "\"refund_amount\":\"" + refund_amount + "\"," +
      "\"out_request_no\":\"" + out_request_no + "\"," +
      "\"refund_reason\":\"reason\"," +
      "\"store_id\":\"" + store_id + "\"," +
      "\"terminal_id\":\"" + terminal_id + "\"}";

    request.setBizContent(biz_content);

    // 如果有界面等待重试退款的处理结果，建议做异步处理，不要在主线程中等待处理结果,不然主线程可能会无响应或等待超时。
    for (int i = 1; i <= retryTimes; i++) {
      try {
        Thread.sleep(5000);

        AlipayServiceEnvConstants.logger.info("refundOrderRetry>>>重试退款请求 第 " + i + " 次");

        AlipayTradeRefundResponse response = alipayClient.execute(request);
        if (null != response && response.isSuccess()) {
          if (response.getCode().equals("10000")) {

            if (response.getFundChange().equals("Y")) {
              // 退款成功,资金有变动,做业务及账务处理
            }

            setResponseMsg("4", response.isSuccess(), response.getCode(), response.getOutTradeNo(),
              response.getTradeNo(), response.getBuyerLogonId(), response.getBody(), "", "", "", "", "");
            break;
          }
        }

        if (i == retryTimes) {
          // 处理到最后一次，还是未退款成功，需要在商户数据库中对此单此次退款最标记，人工介入处理
          setResponseMsg("4", response.isSuccess(), response.getCode(), response.getOutTradeNo(),
            response.getTradeNo(), response.getBuyerLogonId(), response.getBody(), "", "", "", "", "");
        }

      } catch (AlipayApiException e) {
        AlipayServiceEnvConstants.logger.info("refundOrderRetry>>>" + e.getMessage());
      } catch (InterruptedException e) {
        AlipayServiceEnvConstants.logger.info("refundOrderRetry>>>" + e.getMessage());
      }
    }

    AlipayServiceEnvConstants.logger.info("refundOrderRetry>>>End");
  }

  /**
   * 返回前台信息
   *
   * @param success        业务处理状态  true,false
   * @param code           业务处理结果。参照“结果码”
   * @param outTradeNo     商户订单号
   * @param tradeNo        支付宝交易号
   * @param buyerLogonId   买家支付宝账号
   * @param body           返回信息详细列表
   * @param tradeStatus    查询状态
   * @param buyerPayAmount 用户在交易中支付的金额
   * @param totalAmount    订单总金额
   * @param receiptAmount  实收金额
   * @param invoiceAmount  开票金额
   */
  public void setResponseMsg(String service, boolean success, String code, String outTradeNo, String tradeNo,
                             String buyerLogonId, String body, String tradeStatus, String buyerPayAmount,
                             String totalAmount, String receiptAmount, String invoiceAmount) {
    StringBuilder sb = new StringBuilder();
    sb.append("{\"service\":\"" + service + "\",");
    sb.append("\"success\":\"" + success + "\",");
    sb.append("\"code\":\"" + code + "\",");
    sb.append("\"outTradeNo\":\"" + outTradeNo + "\",");
    sb.append("\"tradeNo\":\"" + tradeNo + "\",");
    sb.append("\"buyerLogonId\":\"" + buyerLogonId + "\",");
    sb.append("\"amount\":\"" + amount + "\",");
    sb.append("\"fund_channel\":\"" + fund_channel + "\",");
    sb.append("\"tradeStatus\":\"" + tradeStatus + "\",");
    sb.append("\"buyerPayAmount\":\"" + buyerPayAmount + "\",");
    sb.append("\"totalAmount\":\"" + totalAmount + "\",");
    sb.append("\"receiptAmount\":\"" + receiptAmount + "\",");
    sb.append("\"invoiceAmount\":\"" + invoiceAmount + "\",");
    sb.append("\"body\":[" + body + "]}");

    setResponseBody(sb.toString());
  }

  public String getResponseBody() {
    return responseBody;
  }

  public void setResponseBody(String responseBody) {
    this.responseBody = responseBody;
  }
}
