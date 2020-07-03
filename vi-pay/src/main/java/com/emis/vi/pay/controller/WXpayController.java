package com.emis.vi.pay.controller;

import com.emis.vi.pay.util.emisPropUtil;
import com.emis.vi.pay.util.emisUtil;
import com.emis.vi.pay.wxpay.BridgeForScanPayBusiness;
import com.emis.vi.pay.wxpay.V2.WXPayV2ConfigImpl;
import com.emis.vi.pay.wxpay.V2.bean.emisWXPayStoreSettingBean;
import com.emis.vi.pay.wxpay.V2.emisWXPayStoreSettingKeeper;
import com.emis.vi.pay.wxpay.WXScanPayBusiness;
import com.emis.vi.pay.wxpay.WXScanPayReqData;
import com.tencent.common.Configure;
import com.tencent.protocol.refund_protocol.RefundReqData;
import net.sf.json.JSONObject;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * http://localhost:8080/venus//jsp/ccr/getWXpay.jsp?JSONparam={"attach":"","authCode":"","billDate":"","billType":"","body":"","detail":"","deviceInfo":"","goodsTag":"","opUserID":"","outRefundNo":"","outTradeNo":"00200200012015111917234530225","refundFee":0,"refundFeeType":"","refundID":"","service":"3","spBillCreateIP":"","timeExpire":"","timeStart":"","totalFee":"","transactionID":"","userIp":""}
 */
@RestController
@RequestMapping("/pay")
public class WXpayController {

    @RequestMapping("/getWXpay")
    public String getWXpay(HttpServletRequest request) throws Exception {
        //微信支付系统参数
        String sWXPAY_KEY = emisPropUtil.get("WXPAY_KEY");   //微信私有Key
        String sWXPAY_APPID = emisPropUtil.get("WXPAY_APPID"); //微信分配的公众号ID
        String sWXPAY_MCHID = emisPropUtil.get("WXPAY_MCHID"); //微信支付分配的商户号ID
        String sWXPAY_SUBMCHID = emisPropUtil.get("WXPAY_SUBMCHID"); //受理模式下给子商户分配的子商户号
        String sWXPAY_CERTLOCALPATH = emisPropUtil.get("WXPAY_CERTLOCALPATH"); //HTTPS证书的本地路径
        String sWXPAY_CERTPASSWORD = emisPropUtil.get("WXPAY_CERTPASSWORD");   //HTTPS证书密码，默认密码等于商户号MCHID
        String sWXPAY_MODE = emisPropUtil.get("WXPAY_MODE");   //微信支付模式， mode=空or1(默认)表示读取系统参数; mode=2表示读取Z042作业设置

        ////////////////////////////////////请求参数//////////////////////////////////////
        Hashtable oRequest_ = emisUtil.processRequest(request);
        //接收前台包装成JSON格式的字符串
        String strJSON = (String) oRequest_.get("JSONparam");
        System.out.println("进入微信刷卡支付接口，接收到的参数：" + strJSON);
        String sS_NO = (String) oRequest_.get("S_NO"); // 门店编号, 新版模式时必须
        System.out.println("进入微信刷卡支付接口，接收到的参数S_NO：" + sS_NO);

        BridgeForScanPayBusiness bridge = null;
        if (!"".equals(strJSON)) {
            try {
                bridge = (BridgeForScanPayBusiness) JSONObject.toBean(JSONObject.fromObject(strJSON), BridgeForScanPayBusiness.class);
            } catch (Exception ex) {
                bridge = null;
            }
        }
        if (bridge != null
                && ((!"".equals(sWXPAY_KEY)
                && !"".equals(sWXPAY_APPID) && !"".equals(sWXPAY_MCHID)
                && !"".equals(sWXPAY_CERTLOCALPATH) && !"".equals(sWXPAY_CERTPASSWORD)
                && !"2".equals(sWXPAY_MODE))
                || "2".equals(sWXPAY_MODE))
                ) {
            Logger oLogger = null;
            //微信支付返回结果，JSON格式
            String sResponseBody = "";

            if ("2".equals(sWXPAY_MODE) && !"".equals(sS_NO)) {
                // 模式2：依设置作业
                try {
//                    oLogger = emisLogger.getlog4j(application, "jsp.crr.getWXpay2." + emisUtil.formatDateTime("%y%M%D", emisUtil.now()));
                    oLogger = LogManager.getLogger(WXpayController.class);
                    oLogger.info("----- 微信刷卡支付 mode=2 START -----");
                    oLogger.info("params：" + strJSON);

                    // 初始化参数
//        WXPayV2ConfigImpl wxPayConfig = new WXPayV2ConfigImpl("wxaf8892243bcc83dd", "1445559802", "e0df56979bff5b0c15ea794f69ac11ed", "D:\\cert\\apiclient_cert.p12");
                    emisWXPayStoreSettingBean WXPaySetting = emisWXPayStoreSettingKeeper.getInstance()
                            .getStoreWXPaySetting(request.getServletContext(), sS_NO);
                    WXPayV2ConfigImpl wxPayConfig = new WXPayV2ConfigImpl(WXPaySetting.getWXPAY_APPID(), WXPaySetting.getWXPAY_MCHID(), WXPaySetting.getWXPAY_KEY(), WXPaySetting.getWXPAY_CERTLOCALPATH());
                    oLogger.info("S_NO: " + sS_NO + "; PS_NO：" + WXPaySetting.getPS_NO());

                    if ("1".equals(bridge.getService())) {
                        // 刷卡支付请求
                        oLogger.info("----- 刷卡支付 start -----");
                        HashMap<String, String> data = new HashMap<String, String>();
                        if ("2".equals(WXPaySetting.getWXPAY_MODE())) {
                            data.put("sub_appid", WXPaySetting.getWXPAY_SUBAPPID());
                            data.put("sub_mch_id", WXPaySetting.getWXPAY_SUBMCHID());
                        }
                        data.put("device_info", bridge.getDeviceInfo());
                        data.put("body", bridge.getBody());
//        data.put("detail", "");
                        data.put("attach", bridge.getAttach());
                        data.put("out_trade_no", bridge.getOutTradeNo());
                        data.put("total_fee", String.valueOf(bridge.getTotalFee()));
//        data.put("fee_type", "CNY");
                        data.put("spbill_create_ip", bridge.getSpBillCreateIP());
                        data.put("goods_tag", bridge.getGoodsTag());
//        data.put("limit_pay", "");
                        data.put("auth_code", bridge.getAuthCode());
//        data.put("scene_info", "");

                        WXScanPayBusiness res = new WXScanPayBusiness("V2");
                        res.setLog(oLogger);
                        res.runV2(data, wxPayConfig);
                        sResponseBody = res.getResponseBody();
                        oLogger.info("----- 刷卡支付 end -----");
                    } else if ("2".equals(bridge.getService()) && !"".equals(bridge.getOutTradeNo())) {
                        // 查询订单
                        oLogger.info("----- 查询订单 start -----");
                        HashMap<String, String> data = new HashMap<String, String>();
                        if ("2".equals(WXPaySetting.getWXPAY_MODE())) {
                            data.put("sub_appid", WXPaySetting.getWXPAY_SUBAPPID());
                            data.put("sub_mch_id", WXPaySetting.getWXPAY_SUBMCHID());
                        }
                        data.put("out_trade_no", bridge.getOutTradeNo());

                        WXScanPayBusiness res = new WXScanPayBusiness("V2");
                        res.setLog(oLogger);
                        res.doOnePayQueryV2(data, wxPayConfig);
                        sResponseBody = res.getResponseBody();
                        oLogger.info("----- 查询订单 end -----");
                    } else if ("3".equals(bridge.getService()) && !"".equals(bridge.getOutTradeNo())) {
                        // 撤销订单
                        oLogger.info("----- 撤销订单 start -----");
                        HashMap<String, String> data = new HashMap<String, String>();
                        if ("2".equals(WXPaySetting.getWXPAY_MODE())) {
                            data.put("sub_appid", WXPaySetting.getWXPAY_SUBAPPID());
                            data.put("sub_mch_id", WXPaySetting.getWXPAY_SUBMCHID());
                        }
                        data.put("out_trade_no", bridge.getOutTradeNo());

                        WXScanPayBusiness res = new WXScanPayBusiness("V2");
                        res.setLog(oLogger);
                        res.doOneReverseV2(data, wxPayConfig);
                        sResponseBody = res.getResponseBody();
                        oLogger.info("----- 撤销订单 end -----");
                    } else if ("4".equals(bridge.getService()) && !"".equals(bridge.getOutTradeNo())) {
                        // 申请退款
                        oLogger.info("----- 申请退款 start -----");
                        HashMap<String, String> data = new HashMap<String, String>();
                        if ("2".equals(WXPaySetting.getWXPAY_MODE())) {
                            data.put("sub_appid", WXPaySetting.getWXPAY_SUBAPPID());
                            data.put("sub_mch_id", WXPaySetting.getWXPAY_SUBMCHID());
                        }
                        data.put("transaction_id", bridge.getTransactionID());
                        data.put("out_trade_no", bridge.getOutTradeNo());
                        data.put("device_info", bridge.getDeviceInfo());
                        data.put("out_refund_no", bridge.getOutRefundNo());
                        data.put("total_fee", String.valueOf(bridge.getTotalFee()));
                        data.put("refund_fee", String.valueOf(bridge.getRefundFee()));
                        data.put("op_user_id", bridge.getOpUserID());
//          data.put("refund_fee_type", "CNY");

                        WXScanPayBusiness res = new WXScanPayBusiness("V2");
                        res.setLog(oLogger);
                        res.doRefundV2(data, wxPayConfig);
                        sResponseBody = res.getResponseBody();
                        oLogger.info("----- 申请退款 end -----");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    if (oLogger != null) {
                        oLogger.error(ex.getMessage(), ex);
                    }
                }
            } else {
                // 模式1(原有模式)：依系统参数设置
                try {
//                    oLogger = emisLogger.getlog4j(application, "jsp.crr.getWXpay." + emisUtil.formatDateTime("%y%M", emisUtil.now()));
                    oLogger = LogManager.getLogger(WXpayController.class);
                    oLogger.info("-------------------进入微信刷卡支付 START----------------------");
                    oLogger.info("params：" + strJSON);
                    oLogger.info("S_NO params：" + sS_NO);

                    //初始化参数
                    Configure.setKey(sWXPAY_KEY);//签名算法需要用到的秘钥
                    Configure.setAppID(sWXPAY_APPID);//公众账号ID，成功申请公众账号后获得
                    Configure.setMchID(sWXPAY_MCHID);//商户ID，成功申请微信支付功能之后通过官方发出的邮件获得
                    Configure.setSubMchID(sWXPAY_SUBMCHID);//子商户ID，受理模式下必填；
                    Configure.setCertLocalPath(sWXPAY_CERTLOCALPATH);//HTTP证书在服务器中的路径，用来加载证书用
                    Configure.setCertPassword(sWXPAY_CERTPASSWORD);//HTTP证书的密码，默认等于MCHID

                    //刷卡支付请求：
                    if ("1".equals(bridge.getService())) {
                        oLogger.info("---------------刷卡支付 start-----------------------");
                        // 从bridge里面拿到数据，构建提交被扫支付API需要的数据对象
                        com.emis.vi.pay.wxpay.WXScanPayReqData scanPayReqData = new WXScanPayReqData(
                                //这个是扫码终端设备从用户手机上扫取到的支付授权号，有效期是1分钟
                                bridge.getAuthCode(),
                                //要支付的商品的描述信息，用户会在支付成功页面里看到这个信息
                                bridge.getBody(),
                                //支付订单里面可以填的附加数据，API会将提交的这个附加数据原样返回
                                bridge.getAttach(),
                                //商户系统内部的订单号,32个字符内可包含字母, 确保在商户系统唯一
                                bridge.getOutTradeNo(),
                                //订单总金额，单位为“分”，只能整数
                                bridge.getTotalFee(),
                                //暂时用一分测试
                                //商户自己定义的扫码支付终端设备号，方便追溯这笔交易发生在哪台终端设备上
                                bridge.getDeviceInfo(),
                                //订单生成的机器IP
                                request.getRemoteAddr(),//默认为服务器的IP地址
                                //订单生成时间，格式为yyyyMMddHHmmss，如2015年11月25日9点10分10秒表示为20151125091010
                                bridge.getTimeStart(),
                                //订单失效时间，格式同上
                                bridge.getTimeExpire(),
                                //商品标记，微信平台配置的商品标记，用于优惠券或者满减使用
                                bridge.getGoodsTag()
                        );
                        //刷卡支付
                        WXScanPayBusiness res = new WXScanPayBusiness();
                        res.setLog(oLogger);
                        res.run(scanPayReqData);
                        sResponseBody = res.getResponseBody();
                        oLogger.info("---------------刷卡支付 end-----------------------");
                    } else if ("2".equals(bridge.getService()) && !"".equals(bridge.getOutTradeNo())) {//查询订单
                        oLogger.info("---------------查询订单 start-----------------------");
                        WXScanPayBusiness res = new WXScanPayBusiness();
                        res.setLog(oLogger);
                        res.doOnePayQuery(bridge.getOutTradeNo());
                        sResponseBody = res.getResponseBody();
                        oLogger.info("---------------查询订单 end-----------------------");
                    } else if ("3".equals(bridge.getService()) && !"".equals(bridge.getOutTradeNo())) {//撤销订单
                        oLogger.info("---------------撤销订单 start-----------------------");
                        WXScanPayBusiness res = new WXScanPayBusiness();
                        res.setLog(oLogger);
                        res.doOneReverse(bridge.getOutTradeNo());
                        sResponseBody = res.getResponseBody();
                        oLogger.info("---------------撤销订单 end-----------------------");
                    } else if ("4".equals(bridge.getService()) && !"".equals(bridge.getOutTradeNo())) {
                        oLogger.info("---------------申请退款 start-----------------------");
                        //申请退款
                        //第一步：生成一个业务逻辑对象
                        WXScanPayBusiness res = new WXScanPayBusiness();
                        //第二步：从bridge里面拿到数据，构建提交被扫支付API需要的数据对象
                        RefundReqData refundReqData = new RefundReqData(
                                bridge.getTransactionID(),//是微信系统为每一笔支付交易分配的订单号，通过这个订单号可以标识这笔交易，它由支付订单API支付成功时返回的数据里面获取到。建议优先使用
                                bridge.getOutTradeNo(),//商户系统内部的订单号,transaction_id 、out_trade_no 二选一，如果同时存在优先级：transaction_id>out_trade_no
                                bridge.getDeviceInfo(),//微信支付分配的终端设备号，与下单一致
                                bridge.getOutRefundNo(),//商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
                                bridge.getTotalFee(),//订单总金额，单位为分
                                bridge.getRefundFee(),//退款总金额，单位为分,可以做部分退款
                                bridge.getOpUserID(),//操作员帐号, 默认为商户号
                                bridge.getRefundFeeType()//货币类型，符合ISO 4217标准的三位字母代码，默认为CNY（人民币）
                        );
                        //第三步，执行业务逻辑
                        res.setLog(oLogger);
                        res.doRefund(refundReqData);
                        //第四步；返回json格式的数据回前台
                        sResponseBody = res.getResponseBody();
                        oLogger.info("---------------申请退款 end-----------------------");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    if (oLogger != null) {
                        oLogger.error(e.getMessage(), e);
                    }
                }
            }

            if (oLogger != null) {
                oLogger.info("返回给前台的参数(JSON格式)：");
                oLogger.info(sResponseBody);
                oLogger.info("");
                oLogger.info("----- 进入微信刷卡支付 END -----");
            }
            return sResponseBody;
        }
        return "";
    }
}
