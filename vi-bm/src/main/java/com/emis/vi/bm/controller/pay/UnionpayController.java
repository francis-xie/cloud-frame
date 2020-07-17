package com.emis.vi.bm.controller.pay;

import com.emis.vi.pay.unionpay.PayAction;
import com.emis.vi.pay.unionpay.util.SDKConfig;
import com.emis.vi.pay.util.emisPropUtil;
import com.emis.vi.pay.util.emisUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Hashtable;

@RestController
@RequestMapping("/pay")
public class UnionpayController {

    @RequestMapping("/getUnionpay")
    public String getUnionpay(HttpServletRequest request) throws Exception {
        ////////////////////////////////////系统参数//////////////////////////////////////
        String merId = emisPropUtil.get("UnionPay_MerId").trim(); //银联二维码支付商户代码(必填)
        String backTransUrl = emisPropUtil.get("UnionPay_BackTransUrl").trim(); //银联二维码支付被扫消费、主扫申码、交易撤销、退款接口地址(必填)
        String singleQueryUrl = emisPropUtil.get("UnionPay_SingleQueryUrl").trim(); //银联二维码支付交易状态查询（消费、撤销、退款）接口地址(必填)
        String signCertPath = emisPropUtil.get("UnionPay_SignCertPath").trim(); //银联二维码支付商户私钥证书的本地路径（签名使用，必填）
        String signCertPwd = emisPropUtil.get("UnionPay_SignCertPwd").trim(); //银联二维码支付商户私钥证书的密码（签名使用，必填）
        String encryptCertPath = emisPropUtil.get("UnionPay_EncryptCertPath").trim(); //银联二维码支付商户验签敏感加密证书的本地路径（选填）
        String middleCertPath = emisPropUtil.get("UnionPay_MiddleCertPath").trim(); //银联二维码支付商户验签中级证书的本地路径（必填）
        String rootCertPath = emisPropUtil.get("UnionPay_RootCertPath").trim(); //银联二维码支付商户验签根证书的本地路径（必填）

        System.out.println(merId + "/" + backTransUrl + "/" + singleQueryUrl + "/" + signCertPath + "/" + signCertPwd + "/" + middleCertPath + "/" + rootCertPath + "/" + encryptCertPath);
        if ("".equals(merId) || "".equals(backTransUrl) || "".equals(singleQueryUrl) || "".equals(signCertPath) || "".equals(signCertPwd) || "".equals(middleCertPath) || "".equals(rootCertPath)) {
//            out.clear();
//            out.flush();
//            out.write("{\"respCode\":\"ERROR\",\"respMsg\":\"请检查后台相关参数是否有配置好!\"}");
//            session.invalidate();
            return "{\"respCode\":\"ERROR\",\"respMsg\":\"请检查后台相关参数是否有配置好!\"}";
        }

        SDKConfig.getConfig().setBackRequestUrl(backTransUrl);
        SDKConfig.getConfig().setSingleQueryUrl(singleQueryUrl);
        SDKConfig.getConfig().setVersion("5.1.0");
        SDKConfig.getConfig().setSignMethod("01");
        SDKConfig.getConfig().setIfValidateCNName(false);
        SDKConfig.getConfig().setIfValidateRemoteCert(false);
        SDKConfig.getConfig().setBackUrl("www.specialurl.com");
        SDKConfig.getConfig().setSignCertPath(signCertPath);
        SDKConfig.getConfig().setSignCertPwd(signCertPwd);
        SDKConfig.getConfig().setSignCertType("PKCS12");
        SDKConfig.getConfig().setEncryptCertPath(encryptCertPath);
        SDKConfig.getConfig().setMiddleCertPath(middleCertPath);
        SDKConfig.getConfig().setRootCertPath(rootCertPath);

        ////////////////////////////////////请求参数//////////////////////////////////////
        Hashtable oRequest_ = emisUtil.processRequest(request);
        //访问接口service 1：订单扣款接口、2：订单查询接口、3：订单撤销接口、4：订单退款接口、5：订单退款查询接口
        String service = (String) oRequest_.get("service");
        //C2B码,1-20位数字
        String qrNo = (String) oRequest_.get("qrNo");
        //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
        String orderId = (String) oRequest_.get("orderId");
        //订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
        String txnTime = (String) oRequest_.get("txnTime");
        //交易金额 单位为分，不能带小数点
        String txnAmt = (String) oRequest_.get("txnAmt");
        //终端号
        String termId = (String) oRequest_.get("termId");
        // 原始交易流水号
        String queryId = (String) oRequest_.get("queryId");
        // 原交易商户订单号
        String origOrderId = (String) oRequest_.get("origOrderId");
        // 原交易商户发送交易时间
        String origTxnTime = (String) oRequest_.get("origTxnTime");
        System.out.println(orderId);
        if ("".equals(service) || "".equals(orderId) || "".equals(txnTime)) {
//            out.clear();
//            out.flush();
//            out.write("{\"respCode\":\"ZZZZ\",\"respMsg\":\"传参异常,请联系管理人员!\"}");
//            session.invalidate();
            return "{\"respCode\":\"ZZZZ\",\"respMsg\":\"传参异常,请联系管理人员!\"}";
        }

        Logger logger = null;
        //返回结果， Json 格式
        String sResponseBody = "";
        try {
//            logger = emisLogger.getlog4j(application, "jsp.ccr.getUnionpay." + emisUtil.formatDateTime("%y%M", emisUtil.now()));
            logger = LogManager.getLogger(AlipayController.class);
            logger.info("====================银联二维码支付 START====================");
            logger.info("params：service=[" + service + "],qrNo=[" + qrNo + "],orderId=[" + orderId + "],txnTime=[" + txnTime + "]," +
                    "txnAmt=[" + txnAmt + "],termId=[" + termId + "],queryId=[" + queryId + "],origOrderId=[" + origOrderId + "],origTxnTime=[" + origTxnTime + "]");
            SDKConfig.getConfig().setLogger(logger);
            if ("1".equals(service)) {  //5.2.1 二维码消费
                PayAction p = new PayAction();
                p.consume(merId, qrNo, orderId, txnTime, txnAmt, termId);
                sResponseBody = p.getResponseBody();
            } else if ("2".equals(service)) {  //5.5.1 交易状态查询
                PayAction p = new PayAction();
                p.query(merId, queryId, orderId, txnTime);
                sResponseBody = p.getResponseBody();
            } else if ("3".equals(service)) {  //5.4.1 消费撤销
                PayAction p = new PayAction();
                p.consumeUndo(merId, orderId, txnTime, queryId, origOrderId, origTxnTime, txnAmt, termId);
                sResponseBody = p.getResponseBody();
            } else if ("4".equals(service)) {  //5.4.2 退货
                PayAction p = new PayAction();
                p.refund(merId, orderId, txnTime, queryId, origOrderId, origTxnTime, txnAmt, termId);
                sResponseBody = p.getResponseBody();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }

        logger.info("");
        logger.info("====================银联二维码支付 END====================");
        return sResponseBody;
    }
}
