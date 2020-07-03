package com.emis.vi.pay.wxpay;

import com.tencent.common.Configure;
import com.tencent.common.Signature;
import com.tencent.common.Util;
import com.tencent.common.report.ReporterFactory;
import com.tencent.common.report.protocol.ReportReqData;
import com.tencent.common.report.service.ReportService;
import com.tencent.protocol.downloadbill_protocol.DownloadBillReqData;
import com.tencent.protocol.downloadbill_protocol.DownloadBillResData;
import com.tencent.protocol.pay_protocol.ScanPayReqData;
import com.tencent.protocol.pay_protocol.ScanPayResData;
import com.tencent.protocol.pay_query_protocol.ScanPayQueryReqData;
import com.tencent.protocol.pay_query_protocol.ScanPayQueryResData;
import com.tencent.protocol.refund_protocol.RefundReqData;
import com.tencent.protocol.refund_protocol.RefundResData;
import com.tencent.protocol.reverse_protocol.ReverseReqData;
import com.tencent.protocol.reverse_protocol.ReverseResData;
import com.tencent.service.*;
import com.thoughtworks.xstream.io.StreamException;
import com.emis.vi.pay.wxpay.V2.WXPay;
import com.emis.vi.pay.wxpay.V2.WXPayV2Api;
import com.emis.vi.pay.wxpay.V2.WXPayV2ConfigImpl;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-12-2
 * Time: 上午8:49
 * To change this template use File | Settings | File Templates.
 *
 */
public class WXScanPayBusiness {
  public WXScanPayBusiness() throws IllegalAccessException, ClassNotFoundException, InstantiationException {
    scanPayService = new ScanPayService();
    scanPayQueryService = new ScanPayQueryService();
    reverseService = new ReverseService();
    refundService = new RefundService();//整合退款
    downloadBillService = new DownloadBillService();
  }

  public WXScanPayBusiness(String ver) {

  }

  //打log用
  private Logger log =  null;
  //提交/支付订单
  private ScanPayService scanPayService;
  //查询订单
  private ScanPayQueryService scanPayQueryService;
  //撤销订单
  private ReverseService reverseService;
  //退款
  private RefundService refundService;
  //下载对账单
  private DownloadBillService downloadBillService;
  //返回前台的数据(JSON格式)
  private String responseBody = "";

  /**
   * 直接执行被扫支付业务逻辑（包含最佳实践流程）
   *
   * @param scanPayReqData 这个数据对象里面包含了API要求提交的各种数据字段
   *
   * @throws Exception
   */
  public void run(ScanPayReqData scanPayReqData) throws Exception {

    //--------------------------------------------------------------------
    //构造请求“被扫支付API”所需要提交的数据
    //--------------------------------------------------------------------

    //接受API返回
    String payServiceResponseString;
    long costTimeStart = System.currentTimeMillis();
    payServiceResponseString = scanPayService.request(scanPayReqData);
    long costTimeEnd = System.currentTimeMillis();
    long totalTimeCost = costTimeEnd - costTimeStart;
    log.info("api请求总耗时：" + totalTimeCost + "ms");

    //打印回包数据
    log.info("支付API返回的数据如下：");
    log.info(payServiceResponseString);

    //将从API返回的XML数据映射到Java对象
    ScanPayResData scanPayResData = (ScanPayResData) Util.getObjectFromXML(payServiceResponseString, ScanPayResData.class);

    //异步发送统计请求
    ReportReqData reportReqData = new ReportReqData(
        scanPayReqData.getDevice_info(),
        Configure.PAY_API,
        (int) (totalTimeCost),//本次请求耗时
        scanPayResData.getReturn_code(),
        scanPayResData.getReturn_msg(),
        scanPayResData.getResult_code(),
        scanPayResData.getErr_code(),
        scanPayResData.getErr_code_des(),
        scanPayResData.getOut_trade_no(),
        scanPayReqData.getSpbill_create_ip()
    );
    long timeAfterReport;
    if (Configure.isUseThreadToDoReport()) {
      ReporterFactory.getReporter(reportReqData).run();
      timeAfterReport = System.currentTimeMillis();
      log.info("pay+report总耗时（异步方式上报）：" + (timeAfterReport - costTimeStart) + "ms");
    } else {
      ReportService.request(reportReqData);
      timeAfterReport = System.currentTimeMillis();
      log.info("pay+report总耗时（同步方式上报）：" + (timeAfterReport - costTimeStart) + "ms");
    }
    //把JSON格式发送到前台
    if(scanPayResData != null ){
      //--------------------------------------------------------------------
      //收到API的返回数据的时候得先验证一下数据有没有被第三方篡改，确保安全
      //--------------------------------------------------------------------
      if (!scanPayResData.getReturn_code().equals("FAIL") && !Signature.checkIsSignValidFromResponseString(payServiceResponseString)) {
        scanPayResData.setReturn_msg("Signature");
      }
      JSONObject toJson = JSONObject.fromObject(scanPayResData);
      setResponseBody(toJson.toString());
    }
  }

  /**
   * 进行一次支付订单查询操作
   *
   * @param outTradeNo      商户系统内部的订单号,32个字符内可包含字母, [确保在商户系统唯一]
   * @return 该订单是否支付成功
   * @throws Exception
   */
  public boolean doOnePayQuery(String outTradeNo) throws Exception {
    String payQueryServiceResponseString;

    ScanPayQueryReqData scanPayQueryReqData = new ScanPayQueryReqData("", outTradeNo);
    payQueryServiceResponseString = scanPayQueryService.request(scanPayQueryReqData);

    log.info("支付订单查询API返回的数据如下：");
    log.info(payQueryServiceResponseString);

    //将从API返回的XML数据映射到Java对象
    ScanPayQueryResData scanPayQueryResData = (ScanPayQueryResData) Util.getObjectFromXML(payQueryServiceResponseString, ScanPayQueryResData.class);
    //转成JSON格式
    if(scanPayQueryResData != null){
      //--------------------------------------------------------------------
      //收到API的返回数据的时候得先验证一下数据有没有被第三方篡改，确保安全
      //--------------------------------------------------------------------
      if (!scanPayQueryResData.getReturn_code().equals("FAIL") && !Signature.checkIsSignValidFromResponseString(payQueryServiceResponseString)) {
        scanPayQueryResData.setReturn_msg("Signature");
      }
      JSONObject toJson = JSONObject.fromObject(scanPayQueryResData);
      setResponseBody(toJson.toString());
    }
    return true;
  }

  /**
   * 进行一次撤销操作
   *
   * @param outTradeNo     商户系统内部的订单号,32个字符内可包含字母, [确保在商户系统唯一]
   * @return 该订单是否支付成功
   * @throws Exception
   */
  public boolean doOneReverse(String outTradeNo) throws Exception {
    String reverseResponseString;

    ReverseReqData reverseReqData = new ReverseReqData("", outTradeNo);
    reverseResponseString = reverseService.request(reverseReqData);

    log.info("撤销API返回的数据如下：");
    log.info(reverseResponseString);
    //将从API返回的XML数据映射到Java对象
    ReverseResData reverseResData = (ReverseResData) Util.getObjectFromXML(reverseResponseString, ReverseResData.class);
    //转成JSON格式
    if(reverseResData != null ){
      //--------------------------------------------------------------------
      //收到API的返回数据的时候得先验证一下数据有没有被第三方篡改，确保安全
      //--------------------------------------------------------------------
      if (!reverseResData.getReturn_code().equals("FAIL") && !Signature.checkIsSignValidFromResponseString(reverseResponseString)) {
        reverseResData.setReturn_msg("Signature");
      }
      JSONObject toJson = JSONObject.fromObject(reverseResData);
      setResponseBody(toJson.toString());
    }
    return true;
  }

  /**
   * 调用退款业务逻辑
   * @param refundReqData 这个数据对象里面包含了API要求提交的各种数据字段
   * @throws Exception
   */
  public void doRefund(RefundReqData refundReqData) throws Exception{
    //--------------------------------------------------------------------
    //构造请求“退款API”所需要提交的数据
    //--------------------------------------------------------------------

    //API返回的数据
    String refundServiceResponseString;

    long costTimeStart = System.currentTimeMillis();

    refundServiceResponseString = refundService.request(refundReqData);

    long costTimeEnd = System.currentTimeMillis();
    long totalTimeCost = costTimeEnd - costTimeStart;
    log.info("api请求总耗时：" + totalTimeCost + "ms");

    log.info("退款查询API返回的数据如下：");
    log.info(refundServiceResponseString);

    //将从API返回的XML数据映射到Java对象
    RefundResData refundResData = (RefundResData) Util.getObjectFromXML(refundServiceResponseString, RefundResData.class);

    ReportReqData reportReqData = new ReportReqData(
        refundResData.getDevice_info(),
        Configure.REFUND_API,
        (int) (totalTimeCost),//本次请求耗时
        refundResData.getReturn_code(),
        refundResData.getReturn_msg(),
        refundResData.getResult_code(),
        refundResData.getErr_code(),
        refundResData.getErr_code_des(),
        refundResData.getOut_trade_no(),
        Configure.getIP()
    );

    long timeAfterReport;
    if(Configure.isUseThreadToDoReport()){
      ReporterFactory.getReporter(reportReqData).run();
      timeAfterReport = System.currentTimeMillis();
      log.info("pay+report总耗时（异步方式上报）："+(timeAfterReport-costTimeStart) + "ms");
    }else{
      ReportService.request(reportReqData);
      timeAfterReport = System.currentTimeMillis();
      log.info("pay+report总耗时（同步方式上报）："+(timeAfterReport-costTimeStart) + "ms");
    }
    if (refundResData != null) {
      if (!refundResData.getReturn_code().equals("FAIL") && !Signature.checkIsSignValidFromResponseString(refundServiceResponseString)) {
        refundResData.setReturn_msg("Signature");
      }
      JSONObject toJson = JSONObject.fromObject(refundResData);
      setResponseBody(toJson.toString());
    }
  }

  /**
   * 请求对账单下载服务
   *
   * @param downloadBillReqData 这个数据对象里面包含了API要求提交的各种数据字段
   * @throws Exception
   */
  public void downloadBill(DownloadBillReqData downloadBillReqData) throws Exception {
    //--------------------------------------------------------------------
    //构造请求“对账单API”所需要提交的数据
    //--------------------------------------------------------------------

    //API返回的数据
    String downloadBillServiceResponseString;
    long costTimeStart = System.currentTimeMillis();

    downloadBillServiceResponseString = downloadBillService.request(downloadBillReqData);

    long costTimeEnd = System.currentTimeMillis();
    long totalTimeCost = costTimeEnd - costTimeStart;
    log.info("api请求总耗时：" + totalTimeCost + "ms");

    log.info("对账单API返回的数据如下：");
    log.info(downloadBillServiceResponseString);

    DownloadBillResData downloadBillResData;
    String returnCode = "";
    String returnMsg = "";
    try {
      //注意，这里失败的时候是返回xml数据，成功的时候反而返回非xml数据
      downloadBillResData = (DownloadBillResData) Util.getObjectFromXML(downloadBillServiceResponseString, DownloadBillResData.class);

      if (downloadBillResData == null || downloadBillResData.getReturn_code() == null) {
        log.error("Case1:对账单API请求逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问");
        return;
      }
      if (downloadBillResData.getReturn_code().equals("FAIL")) {
        ///注意：一般这里返回FAIL是出现系统级参数错误，请检测Post给API的数据是否规范合法
        //log.error("Case2:对账单API系统返回失败，请检测Post给API的数据是否规范合法");
        returnCode = "FAIL";
        returnMsg = downloadBillResData.getReturn_msg();
      }
      JSONObject toJson = JSONObject.fromObject(downloadBillResData);
      setResponseBody(toJson.toString());
    } catch (StreamException e) {
      //注意，这里成功的时候是直接返回纯文本的对账单文本数据，非XML格式
      if (downloadBillServiceResponseString == null || downloadBillServiceResponseString.equals("")) {
        log.info("Case4:对账单API系统返回数据为空");
      } else {
        log.info("Case3:对账单API系统成功返回数据");
        setResponseBody(downloadBillServiceResponseString);
      }
      returnCode = "SUCCESS";
    } finally {
      ReportReqData reportReqData = new ReportReqData(
        downloadBillReqData.getDevice_info(),
        Configure.DOWNLOAD_BILL_API,
        (int) (totalTimeCost),//本次请求耗时
        returnCode,
        returnMsg,
        "",
        "",
        "",
        "",
        Configure.getIP()
      );

      long timeAfterReport;
      if (Configure.isUseThreadToDoReport()) {
        ReporterFactory.getReporter(reportReqData).run();
        timeAfterReport = System.currentTimeMillis();
        log.info("pay+report总耗时（异步方式上报）：" + (timeAfterReport - costTimeStart) + "ms");
      } else {
        ReportService.request(reportReqData);
        timeAfterReport = System.currentTimeMillis();
        log.info("pay+report总耗时（同步方式上报）：" + (timeAfterReport - costTimeStart) + "ms");
      }
    }
  }

  public void setScanPayService(ScanPayService service) {
    scanPayService = service;
  }

  public void setScanPayQueryService(ScanPayQueryService service) {
    scanPayQueryService = service;
  }

  public void setReverseService(ReverseService service) {
    reverseService = service;
  }

  public String getResponseBody() {
    return responseBody;
  }

  public void setResponseBody(String responseBody) {
    this.responseBody = responseBody;
  }

  public Logger getLog() {
    return log;
  }

  public void setLog(Logger log) {
    this.log = log;
  }

  /**
   * 刷卡支付-V2版本(多帐号版)
   * @param data
   * @param wxPayConfig
   * @throws Exception
   */
  public void runV2(HashMap<String, String> data, WXPayV2ConfigImpl wxPayConfig) throws Exception {
    //--------------------------------------------------------------------
    //构造请求“被扫支付API”所需要提交的数据
    //--------------------------------------------------------------------

    //接受API返回
    String payServiceResponseString;
    long costTimeStart = System.currentTimeMillis();
    WXPayV2Api wxApi = new WXPayV2Api(wxPayConfig);
    wxApi.setoLogger_(log);
    payServiceResponseString = wxApi.doMicroPayRtnXmlData(data);
    long costTimeEnd = System.currentTimeMillis();
    long totalTimeCost = costTimeEnd - costTimeStart;
    log.info("api请求总耗时：" + totalTimeCost + "ms");

    //打印回包数据
//    log.info("支付API返回的数据如下：");
//    log.info(payServiceResponseString);

    //将从API返回的XML数据映射到Java对象
    ScanPayResData scanPayResData = (ScanPayResData) Util.getObjectFromXML(payServiceResponseString, ScanPayResData.class);

    //异步发送统计请求
    // 统计请求在V2接口中已处理，此处不再调用

    //把JSON格式发送到前台
    if(scanPayResData != null ){
      WXPay wxpay = new WXPay(wxPayConfig);
      //--------------------------------------------------------------------
      //收到API的返回数据的时候得先验证一下数据有没有被第三方篡改，确保安全
      //--------------------------------------------------------------------
      if (!scanPayResData.getReturn_code().equals("FAIL") && !wxpay.isResponseSignatureValidXmlData(payServiceResponseString)) {
        scanPayResData.setReturn_msg("Signature");
      }
      JSONObject toJson = JSONObject.fromObject(scanPayResData);
      setResponseBody(toJson.toString());
    }
  }

  /**
   * 进行一次支付订单查询操作-V2版本(多帐号版)
   * @param data
   * @param wxPayConfig
   * @return
   * @throws Exception
   */
  public boolean doOnePayQueryV2(HashMap<String, String> data, WXPayV2ConfigImpl wxPayConfig) throws Exception {
    String payQueryServiceResponseString;

    WXPayV2Api wxApi = new WXPayV2Api(wxPayConfig);
    wxApi.setoLogger_(log);
    payQueryServiceResponseString = wxApi.doOrderQueryRtnXmlData(data);

//    log.info("支付订单查询API返回的数据如下：");
//    log.info(payQueryServiceResponseString);

    //将从API返回的XML数据映射到Java对象
    ScanPayQueryResData scanPayQueryResData = (ScanPayQueryResData) Util.getObjectFromXML(payQueryServiceResponseString, ScanPayQueryResData.class);
    //转成JSON格式
    if(scanPayQueryResData != null){
      WXPay wxpay = new WXPay(wxPayConfig);
      //--------------------------------------------------------------------
      //收到API的返回数据的时候得先验证一下数据有没有被第三方篡改，确保安全
      //--------------------------------------------------------------------
      if (!scanPayQueryResData.getReturn_code().equals("FAIL") && !wxpay.isResponseSignatureValidXmlData(payQueryServiceResponseString)) {
        scanPayQueryResData.setReturn_msg("Signature");
      }
      JSONObject toJson = JSONObject.fromObject(scanPayQueryResData);
      setResponseBody(toJson.toString());
    }
    return true;
  }

  /**
   * 进行一次撤销操作-V2版本(多帐号版)
   * @param data
   * @param wxPayConfig
   * @return
   * @throws Exception
   */
  public boolean doOneReverseV2(HashMap<String, String> data, WXPayV2ConfigImpl wxPayConfig) throws Exception {
    String reverseResponseString;

    WXPayV2Api wxApi = new WXPayV2Api(wxPayConfig);
    wxApi.setoLogger_(log);
    reverseResponseString = wxApi.doReverseRtnXmlData(data);

//    log.info("撤销API返回的数据如下：");
//    log.info(reverseResponseString);
    //将从API返回的XML数据映射到Java对象
    ReverseResData reverseResData = (ReverseResData) Util.getObjectFromXML(reverseResponseString, ReverseResData.class);
    //转成JSON格式
    if(reverseResData != null ){
      WXPay wxpay = new WXPay(wxPayConfig);
      //--------------------------------------------------------------------
      //收到API的返回数据的时候得先验证一下数据有没有被第三方篡改，确保安全
      //--------------------------------------------------------------------
      if (!reverseResData.getReturn_code().equals("FAIL") && !wxpay.isResponseSignatureValidXmlData(reverseResponseString)) {
        reverseResData.setReturn_msg("Signature");
      }
      JSONObject toJson = JSONObject.fromObject(reverseResData);
      setResponseBody(toJson.toString());
    }
    return true;
  }

  /**
   * 调用退款业务逻辑-V2版本(多帐号版)
   * @param data
   * @param wxPayConfig
   * @throws Exception
   */
  public void doRefundV2(HashMap<String, String> data, WXPayV2ConfigImpl wxPayConfig) throws Exception{
    //--------------------------------------------------------------------
    //构造请求“退款API”所需要提交的数据
    //--------------------------------------------------------------------

    //API返回的数据
    String refundServiceResponseString;

    long costTimeStart = System.currentTimeMillis();

    WXPayV2Api wxApi = new WXPayV2Api(wxPayConfig);
    wxApi.setoLogger_(log);
    refundServiceResponseString = wxApi.doRefundRtnXmlData(data);

    long costTimeEnd = System.currentTimeMillis();
    long totalTimeCost = costTimeEnd - costTimeStart;
    log.info("api请求总耗时：" + totalTimeCost + "ms");

//    log.info("退款查询API返回的数据如下：");
//    log.info(refundServiceResponseString);

    //将从API返回的XML数据映射到Java对象
    RefundResData refundResData = (RefundResData) Util.getObjectFromXML(refundServiceResponseString, RefundResData.class);

    if (refundResData != null) {
      WXPay wxpay = new WXPay(wxPayConfig);
      if (!refundResData.getReturn_code().equals("FAIL") && !wxpay.isResponseSignatureValidXmlData(refundServiceResponseString)) {
        refundResData.setReturn_msg("Signature");
      }
      JSONObject toJson = JSONObject.fromObject(refundResData);
      setResponseBody(toJson.toString());
    }
  }

  /**
   * 请求对账单下载服务-V2版本(多帐号版)
   *
   * @param data
   * @param wxPayConfig
   * @throws Exception
   */
  public void downloadBillV2(HashMap<String, String> data, WXPayV2ConfigImpl wxPayConfig) throws Exception {
    //--------------------------------------------------------------------
    //构造请求“对账单API”所需要提交的数据
    //--------------------------------------------------------------------

    //API返回的数据
    String downloadBillServiceResponseString;
    long costTimeStart = System.currentTimeMillis();

    WXPayV2Api wxApi = new WXPayV2Api(wxPayConfig);
    wxApi.setoLogger_(log);
    downloadBillServiceResponseString = wxApi.downloadBillRtnXmlData(data);

    long costTimeEnd = System.currentTimeMillis();
    long totalTimeCost = costTimeEnd - costTimeStart;
    log.info("api请求总耗时：" + totalTimeCost + "ms");

    //log.info("对账单API返回的数据如下：");
    //log.info(downloadBillServiceResponseString);

    DownloadBillResData downloadBillResData;
    String returnCode = "";
    String returnMsg = "";
    try {
      //注意，这里失败的时候是返回xml数据，成功的时候反而返回非xml数据
      downloadBillResData = (DownloadBillResData) Util.getObjectFromXML(downloadBillServiceResponseString, DownloadBillResData.class);

      if (downloadBillResData == null || downloadBillResData.getReturn_code() == null) {
        log.error("Case1:对账单API请求逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问");
        return;
      }
      if (downloadBillResData.getReturn_code().equals("FAIL")) {
        ///注意：一般这里返回FAIL是出现系统级参数错误，请检测Post给API的数据是否规范合法
        //log.error("Case2:对账单API系统返回失败，请检测Post给API的数据是否规范合法");
        returnCode = "FAIL";
        returnMsg = downloadBillResData.getReturn_msg();
      }
      JSONObject toJson = JSONObject.fromObject(downloadBillResData);
      setResponseBody(toJson.toString());
    } catch (StreamException e) {
      //注意，这里成功的时候是直接返回纯文本的对账单文本数据，非XML格式
      if (downloadBillServiceResponseString == null || downloadBillServiceResponseString.equals("")) {
        log.info("Case4:对账单API系统返回数据为空");
      } else {
        log.info("Case3:对账单API系统成功返回数据");
        setResponseBody(downloadBillServiceResponseString);
      }
      returnCode = "SUCCESS";
    }
  }
}