/**   
 * @Project: pay-api 
 * @Title: JsonUtils.java 
 * @Package com.smk.util 
 * @Description: 
 * @author yang_df 
 * @date 2016年8月17日
 * @Copyright: 2016 www.96225.com Inc. All rights reserved. 
 * @version V1.1.0   
 */
package com.emis.vi.pay.hzsmk.util;

import java.util.Map;

import com.alibaba.fastjson.JSON;

import net.sf.json.JSONObject;

/**
 * @Package com.smk.util
 * @ClassName: JsonUtils
 * @Description:
 * @author yang_df
 * @since 2016年8月17日
 * @change yang_df @date 2016年8月17日 @Description
 * @version V1.1.0
 */
public class JsonUtils {
	
	/**
	 * @Title: parseMapToJson
	 * @Description:map转json，map为string，Object类型
	 * @author yang_df
	 * @since 2016年8月17日
	 * @change yang_df @date 2016年8月17日 @Description:
	 * @param map
	 * @return
	 */
	public static String parseMapObjToJson(Map<String, Object> map) {
		if (map == null) {
			return null;
		}
		String json = null;
		JSONObject jsonObject = JSONObject.fromObject(map);
		json = jsonObject.toString();
		return json;
	}
	
	
	public static Map<String, Object> paseJsonToMap(String json) {
		return JSON.parseObject(json, Map.class);
	}



	
	
}
