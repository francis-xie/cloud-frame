package com.emis.vi.pay.verypay.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * 签名
 * 
 * @author March
 * @date 2017/8/10
 * @version 1.1.0
 */
public class Signature {

	/**
	 * 签名算法
	 * 
	 * @param map
	 *            要参与签名的数据
	 * @param app_secret
	 *            签名需要用到
	 * @return 签名
	 */
	public static String getSign(Map<String, Object> map, String app_secret) {
		String result = getSignParams(map);
		result += app_secret;
		result = MD5.MD5Encode(result);
		return result;
	}

	/**
	 * 把map对象转换post参数格式a=1&b=2
	 * 
	 * @param map
	 *            要参与签名的数据对象
	 * @return post参数格式
	 */
	public static String getSignParams(Map<String, Object> map) {
		ArrayList<String> list = new ArrayList<String>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey().toString();
			if (entry.getValue() == null)
				continue;
			String value = entry.getValue().toString();//.replace("\"", "");
			if (key.toLowerCase().equals("sign")) {
				continue;
			} else if (value.trim().length() == 0) {
				continue;
			} else if (value.equals("null")) {
				continue;
			}
			if (entry.getValue() != "") {
				list.add(key + "=" + value.toString() + "&");
			}
		}

		int size = list.size();
		String[] arrayToSort = list.toArray(new String[size]);
		Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			sb.append(arrayToSort[i]);
		}
		String result = sb.toString();
		int resultLen = result.length();
		result = result.substring(0, resultLen - 1).trim();
		System.out.println(result);
		return result;
	}

	/**
	 * 把map对象转换post参数格式a=1&b=2
	 * 
	 * @param map
	 *            要参与签名的数据对象
	 * @return post参数格式
	 */
	public static String getPostParams(Map<String, Object> map) {
		ArrayList<String> list = new ArrayList<String>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String Key = entry.getKey().toString();
			String Value = entry.getValue().toString().replace("\"", "");
			if (entry.getValue() != "") {
				list.add(Key + "=" + Value.toString() + "&");
			}
		}

		int size = list.size();
		String[] arrayToSort = list.toArray(new String[size]);
		Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			sb.append(arrayToSort[i]);
		}
		String result = sb.toString();
		int resultLen = result.length();
		result = result.substring(0, resultLen - 1).trim();
		return result;
	}
}