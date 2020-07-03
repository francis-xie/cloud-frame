package com.emis.vi.pay.hzsmk.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.emis.vi.pay.hzsmk.constant.TradeConstant;


public class TradeSignFieldsUtil {
	

	public static String getSignMsg(Map<String, Object> map) {
		List<String> signFields = getSignFields((String) map.get("tradeCode"));
		if (signFields.size() < 1) {
			return null;
		}
		StringBuffer signMsg = new StringBuffer();
		for (String field : signFields) {
			if (signMsg.length() == 0) {
				signMsg.append(field).append(map.get(field) == null ? "" : map.get(field));
			} else {
				signMsg.append("&").append(field).append(map.get(field) == null ? "" : map.get(field));
			}
		}
		return signMsg.toString();
	}

	/**
	 * @Title: getSignFields
	 * @Description: 获取签名字段
	 * @author yang_df
	 * @since 2016年8月26日
	 * @change yang_df @date 2016年8月26日 @Description:
	 * @param tradeCode
	 * @return
	 */
	private static List<String> getSignFields(String tradeCode) {
		List<String> resultList = new ArrayList<String>();
		resultList.add("version");
		resultList.add("tradeCode");
		resultList.add("reqSeq");
		resultList.add("merCode");
		resultList.add("channelNo");
		resultList.add("tradeDate");
		resultList.add("tradeTime");
		if (TradeConstant.QUERY_MER_BALANCE.equals(tradeCode)) {
		} else if (TradeConstant.SINGLE_AGENT_PAY.equals(tradeCode)) {
			resultList.add("orderNo");
			resultList.add("busType");
			resultList.add("toibkn");
			resultList.add("actacn");
			resultList.add("toname");
			resultList.add("toaddr");
			resultList.add("tobknm");
			resultList.add("amount");
			resultList.add("currency");
			resultList.add("remark");
		} else if (TradeConstant.BATCH_AGENT_PAY.equals(tradeCode)) {
			resultList.remove("reqSeq");
			resultList.add("busType");
			resultList.add("orderNo");
		} else if (TradeConstant.QUERY_AGENT_PAY_RESULT.equals(tradeCode)) {
			resultList.remove("tradeDate");
			resultList.remove("tradeTime");
			resultList.add("orderNo");
			resultList.add("orgSeq");
		} else if (TradeConstant.DOWNLOAD_CHECK_FILE.equals(tradeCode)) {
			resultList.remove("tradeDate");
			resultList.remove("tradeTime");
			resultList.add("tradeType");
			resultList.add("busType");
			resultList.add("date");
		} else {
			return new ArrayList<String>();
		}
		return resultList;
	}

}
