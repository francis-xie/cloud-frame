package com.emis.vi.pay.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

/**
 * HttpClient 连线请求抽象类
 * 
 * @author Francis.Xie
 **/
public abstract class HttpClientService {
	/**
	 * 连接超时时间(毫秒)，默认20秒
	 */
	protected int iTimeOut = 20 * 1000;
	/**
	 * 编码
	 */
	protected String charset = "UTF-8";
	/**
	 * 连线后是否需要响应返回处理
	 */
	protected boolean isNeedResponse = true;
	/**
	 * 是否需要列印LOG
	 */
	protected boolean isNeedLog = true;
	/**
	 * 是否https方式连线，忽略校验过程、不需要证书模式
	 */
	protected boolean isSSL = false;
	/**
	 * 请求传参额外数据
	 */
	protected Map<String, Object> requestMap = new HashMap<String, Object>();
	/**
	 * 响应返回额外数据
	 */
	protected Map<String, Object> responseMap = new HashMap<String, Object>();

	/**
	 * POST方式连线（需要输入自己完整的URL地址）
	 * 
	 * @param sUrl 连线URL网址
	 * @param oPm 请求参数
	 * @return 连线返回值
	 * @throws Exception
	 */
	public abstract String doPost(String sUrl, List<NameValuePair> oPm) throws Exception;

	/**
	 * GET方式连线（需要输入自己完整的URL地址）
	 * 
	 * @param sUrl 连线URL网址
	 * @param sParameter 请求参数
	 * @return 连线返回值
	 * @throws Exception
	 */
	public abstract String doGet(String sUrl, String sParameter) throws Exception;

	/**
	 * 连接超时时间(毫秒)，默认20秒
	 */
	public void setiTimeOut(int iTimeOut) {
		this.iTimeOut = iTimeOut;
	}

	/**
	 * 编码
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}

	/**
	 * 连线后是否需要响应返回处理
	 */
	public void setNeedResponse(boolean isNeedResponse) {
		this.isNeedResponse = isNeedResponse;
	}

	/**
	 * 是否需要列印LOG
	 */
	public void setNeedLog(boolean isNeedLog) {
		this.isNeedLog = isNeedLog;
	}

	/**
	 * 是否https方式连线，忽略校验过程、不需要证书模式
	 */
	public void setSSL(boolean isSSL) {
		this.isSSL = isSSL;
	}

	/**
	 * 请求传参额外数据
	 */
	public Map<String, Object> getRequestMap() {
		return requestMap;
	}

	/**
	 * 响应返回额外数据
	 */
	public Map<String, Object> getResponseMap() {
		return responseMap;
	}

}
