package com.emis.vi.pay.hzsmk.payAction;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import com.emis.vi.pay.hzsmk.util.ConstantUtil;
import com.emis.vi.pay.hzsmk.util.JsonUtils;
import com.emis.vi.pay.hzsmk.util.SmkSign;
import com.emis.vi.pay.hzsmk.util.http.HttpClientUtil;

/*
 * demo
 */
public class PayTest {

	private final String PWD = "smk123456";
	private final String PFX_PATH = "D:/home/smk/smk_agent.pfx";

	/**付款码扫码支付
	 * @throws Exception */
	@Test
	public void qrcPay() throws Exception {
		String URL = "http://192.168.23.188:9080/ifsp-uaesway/qrc/qrcApply";
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("appId", "48000201609183161321824365858198");
		reqMap.put("txnCode", "AM1002");
		reqMap.put("signType", "RSA");
		reqMap.put("merId", "000010");
		reqMap.put("merStoreId", "00001001");
		reqMap.put("nonceStr", "1111111111");
		reqMap.put("orderId", "20160929093300000000000000000014");
		reqMap.put("orderTm", "20160929093306");
		reqMap.put("txnTokenCode", "527312291979581554");
		reqMap.put("txnDes", "付款码支付");
		reqMap.put("txnAmt", "100");
        String signData = ConstantUtil.coverMap2String(reqMap);
        System.out.println("对参数按照key=value的格式，并按照参数名ASCII字典序排序生成字符串：[ " + signData + " ]");
        String signAture = SmkSign.sign(signData, PFX_PATH, PWD);
        System.out.println("签名值：[" + signAture + "]");
        reqMap.put("signAture", signAture);
		
		String params = JsonUtils.parseMapObjToJson(reqMap);
		
		String result = HttpClientUtil.sendMsgHTTP(params, URL, "");
		
		System.out.println(result);
		
		//返回码
		Map<String, Object> resultMap = JsonUtils.paseJsonToMap(result);
		if ("0000".equals(resultMap.get("respCode"))) {
			boolean flag = SmkSign.verifySignRSA(resultMap);
	        //验签
			if (flag) {
				System.out.println("验签成功");
			} else {
				System.out.println("验签失败");
			}
		}
	}
	
	/**
	 * 二维码申请
	 * @throws Exception
	 */
	@Test
	public void qrcApply() throws Exception {
		String URL = "http://192.168.23.188:9080/ifsp-uaesway/qrc/qrcApply";
		//用户信息
        Map<String, Object> userMap = new HashMap<String, Object>();
        userMap.put("userId", "143711246508776");
        userMap.put("userName", "张三");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("appId", "48000201609183161321824365858198");
        map.put("txnCode", "QR1001");
        map.put("signType", "MD5");
        map.put("qrcTypeNo", "QR01");
        map.put("merId", "000010");
        map.put("qrcTxnInfo", JsonUtils.parseMapObjToJson(userMap));
        map.put("orderId", "20160929103100000000000000000003");
        map.put("orderTm", "20160929103102");
        String signData = ConstantUtil.coverMap2String(map);
        System.out.println("对参数按照key=value的格式，并按照参数名ASCII字典序排序生成字符串：[ " + signData + " ]");
        signData = signData + "&key=917EA87E4375479F9D9067EAF17A4528";
        System.out.println("signData:" + signData);
        String signAture = SmkSign.MD5Encoder(signData, "UTF-8").toUpperCase();
        
//        String signAture = SmkSign.sign(signData, PFX_PATH, PWD);
        System.out.println("签名值：[" + signAture + "]");
        map.put("signAture", signAture);
		
		String params = JsonUtils.parseMapObjToJson(map);
		
		String result = HttpClientUtil.sendMsgHTTP(params, URL, "");
		//返回码
		Map<String, Object> resultMap = JsonUtils.paseJsonToMap(result);
		
		if ("0000".equals(resultMap.get("respCode"))) {
			boolean flag = SmkSign.verifySignMD5(resultMap, "917EA87E4375479F9D9067EAF17A4528");
//			boolean flag = SmkSign.verifySignRSA(resultMap);
	        //验签
			if (flag) {
				System.out.println("验签成功");
			} else {
				System.out.println("验签失败");
			}
		}
	}
}
