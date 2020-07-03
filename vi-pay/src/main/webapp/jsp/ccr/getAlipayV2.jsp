<%--  $Id: getAlipayV2.jsp 10829 2018-01-26 09:21:01Z francis.xie $
  2015/06/10 Francis.xie Modify 支付宝条码支付 升级2.0
  2018/01/19 Francis.xie Modify 需求 #42037 [标准版-元朗]支持新版支付宝 支持参数设定RSA签名
  2018/01/26 Francis.xie Modify 调整传过来的商品参数goods_detail中有空值商品编号和名称时替换成参数里面的
  service，1：条码支付请求、2：查询订单、3：撤销订单、4：申请退款
  http://127.0.0.1:8052/venus/jsp/ccr/getAlipayV2.jsp?service=2&out_trade_no=520521520521120150623163530562
  http://127.0.0.1:8052/venus/jsp/ccr/getAlipayV2.jsp?service=3&out_trade_no=520521520521120150623163530562
  http://127.0.0.1:8052/venus/jsp/ccr/getAlipayV2.jsp?service=4&out_trade_no=999999999999120150625103529620&trade_no=2015062521001004200039844461&refund_amount=0.50
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="com.emis.vi.pay.util.emisUtil" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="java.net.URLDecoder" %>
<%@ page import="com.emis.vi.pay.alipay.f2fpay.*" %>
<%@ page import="com.emis.vi.pay.alipay.constants.AlipayServiceEnvConstants" %>
<%@ page import="com.emis.vi.pay.util.emisPropUtil" %>
<%@ page import="org.springframework.beans.factory.annotation.Autowired" %>
<%@ page import="org.springframework.jdbc.core.JdbcTemplate" %>
<%@ page import="org.apache.log4j.LogManager" %>
<%!
  @Autowired
  private JdbcTemplate jdbcTemplate;
%>
<%
  ////////////////////////////////////系统参数//////////////////////////////////////
  String sUrl = emisPropUtil.get("ALIPAY_URL2").trim();  //支付宝网关地址
  String sALIPAY_APP_ID = emisPropUtil.get("ALIPAY_APP_ID").trim(); //支付宝分配给开发者的应用Id
  String sALIPAY_PRIVATE_KEY = emisPropUtil.get("ALIPAY_PRIVATE_KEY").trim(); //RSA私钥
  String sALIPAY_PUBLIC_KEY = emisPropUtil.get("ALIPAY_PUBLIC_KEY").trim(); //支付宝公钥
  String sALIPAY_P_NO = emisPropUtil.get("ALIPAY_P_NO").trim(); //支付宝商品编号
  String sALIPAY_P_NAME = emisPropUtil.get("ALIPAY_P_NAME").trim(); //支付宝商品名称
  String sALIPAY_PROVIDER_ID = emisPropUtil.get("ALIPAY_PROVIDER_ID").trim(); //系统商编号
  String sALIPAY_SIGN_TYPE = emisPropUtil.get("ALIPAY_SIGN_TYPE").trim(); //签名算法RSA/RSA2

  sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
  sALIPAY_PROVIDER_ID = URLDecoder.decode(new String(decoder.decodeBuffer(sALIPAY_PROVIDER_ID), "UTF-8"), "UTF-8");

  if (request.getParameter("service") != null && request.getParameter("out_trade_no") != null && !"".equals(sUrl)
    && !"".equals(sALIPAY_APP_ID) && !"".equals(sALIPAY_PRIVATE_KEY) && !"".equals(sALIPAY_PUBLIC_KEY)
    && !"".equals(sALIPAY_P_NO) && !"".equals(sALIPAY_P_NAME)) {
    Logger logger = null;
    ////////////////////////////////////请求参数//////////////////////////////////////
    Hashtable oRequest_ = emisUtil.processRequest(request);
    //访问接口，1：条码支付请求、2：查询订单、3：撤销订单、4：申请退款，必填
    String service = (String) oRequest_.get("service");
    //商户订单号，商户网站订单系统中唯一订单号，必填
    String out_trade_no = (String) oRequest_.get("out_trade_no");
    //支付宝交易号，支付宝交易凭证号，申请退款时必填
    String trade_no = (String) oRequest_.get("trade_no");
    //卖家支付宝用户号
    String seller_id = emisPropUtil.get("ALIPAY_SELLER_ID").trim();
    seller_id = URLDecoder.decode(new String(decoder.decodeBuffer(seller_id), "UTF-8"), "UTF-8");
    //支付宝返回结果， Json 格式
    String sResponseBody = "";

    try {
//      logger = emisLogger.getlog4j(application, "jsp.ccr.getAlipayV2." + emisUtil.formatDateTime("%y%M", emisUtil.now()));
      logger = LogManager.getLogger("jsp.ccr.getAlipayV2." + emisUtil.formatDateTime("%y%M", emisUtil.now()));
      logger.info("params：service=[" + service + "],out_trade_no=[" + out_trade_no + "]," +
        "trade_no=[" + trade_no + "],seller_id=[" + seller_id + "]");
      AlipayServiceEnvConstants.logger = logger;
      AlipayServiceEnvConstants.ALIPAY_PUBLIC_KEY = sALIPAY_PUBLIC_KEY;
      AlipayServiceEnvConstants.APP_ID = sALIPAY_APP_ID;
      AlipayServiceEnvConstants.PRIVATE_KEY = sALIPAY_PRIVATE_KEY;
      AlipayServiceEnvConstants.ALIPAY_GATEWAY = sUrl;
      AlipayServiceEnvConstants.goods_id = sALIPAY_P_NO;
      AlipayServiceEnvConstants.goods_name = sALIPAY_P_NAME;
      AlipayServiceEnvConstants.sys_service_provider_id = sALIPAY_PROVIDER_ID;
      if (sALIPAY_SIGN_TYPE != null && !"".equals(sALIPAY_SIGN_TYPE))
        AlipayServiceEnvConstants.ALIPAY_SIGN_TYPE = sALIPAY_SIGN_TYPE;

      if ("1".equals(service)) {  //条码支付请求：alipay.trade.pay
        //支付授权码，用户支付宝钱包中的“付款码”信息，必填
        String auth_code = (String) oRequest_.get("auth_code");
        //付款金额，订单总金额，必填
        String total_amount = (String) oRequest_.get("total_amount");
        //订单标题，必填
        String subject = (String) oRequest_.get("subject");
        //商品明细列表
        String goods_detail = (String) oRequest_.get("goods_detail");
        if (goods_detail != null && !"".equals(goods_detail)) {
          goods_detail = goods_detail.replaceAll("\"goods_id\":\"\"", "\"goods_id\":\"" + sALIPAY_P_NO + "\"")
            .replaceAll("\"goods_name\":\"\"", "\"goods_name\":\"" + sALIPAY_P_NAME + "\"");  //商品明细列表
        }
        //商户操作员编号
        String operator_id = (String) oRequest_.get("operator_id");
        //商户门店编号
        String store_id = (String) oRequest_.get("store_id");
        //机具终端编号
        String terminal_id = (String) oRequest_.get("terminal_id");
        //支付宝的店铺编号
        String   alipay_store_id="";
       String qryWS=" select KOUBEI_ID from WAIMAI_STORE  where S_NO ='"+store_id+"' and IS_KOUBEI ='Y'";
       try {
         alipay_store_id = jdbcTemplate.queryForObject(qryWS, String.class);
       }catch (Exception e){
         e.printStackTrace();
       }finally {

       }
        //////////////////////////////////////////////////////////////////////////////////
        //把请求参数打包成数组
        ToAlipayBarTradePay res = new ToAlipayBarTradePay();
        res.barPay(out_trade_no, auth_code, total_amount, subject, seller_id,
          goods_detail, operator_id, store_id, terminal_id,alipay_store_id);
        sResponseBody = res.getResponseBody();
      } else if ("2".equals(service)) { //查询订单：alipay.trade.query
        //////////////////////////////////////////////////////////////////////////////////
        ToAlipayBarTradePay res = new ToAlipayBarTradePay();
        res.query(out_trade_no);
        sResponseBody = res.getResponseBody();
      } else if ("3".equals(service)) { //撤销订单：alipay.trade.cancel
        //////////////////////////////////////////////////////////////////////////////////
        ToAlipayBarTradePay res = new ToAlipayBarTradePay();
        res.cancelOrder(out_trade_no);
        sResponseBody = res.getResponseBody();
      } else if ("4".equals(service) && !"".equals(trade_no)) { //申请退款：alipay.trade.refund
        //////////////////////////////////////////////////////////////////////////////////
        //退款金额，需要退款的金额，该金额不能大于订单金额，必填
        String refund_amount = (String) oRequest_.get("refund_amount");
        //商户门店编号
        String store_id = (String) oRequest_.get("store_id");
        //机具终端编号
        String terminal_id = (String) oRequest_.get("terminal_id");

        ToAlipayBarTradePay res = new ToAlipayBarTradePay();
        res.refundOrder(trade_no, refund_amount, "", store_id, terminal_id);
        sResponseBody = res.getResponseBody();
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }

    logger.info(sResponseBody);
    logger.info("");
    logger.info("");
    out.clear();
    out.flush();
    out.write(sResponseBody);
    session.invalidate();
    return;
  }
%>