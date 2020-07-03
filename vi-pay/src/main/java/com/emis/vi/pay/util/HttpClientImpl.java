package com.emis.vi.pay.util;

import java.util.List;

import org.apache.commons.io.IOUtils;
import com.emis.vi.pay.util.log4j.LogKit;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

/**
 * HttpClient 连线请求实现类
 *
 * @author Francis.Xie
 **/
public class HttpClientImpl extends HttpClientService {

    @Override
    public String doPost(String sUrl, List<NameValuePair> oPm) throws Exception {
        if (sUrl == null || "".equals(sUrl.trim())) { //$NON-NLS-1$
            LogKit.error("url is null");
            return "";
        }

        HttpClient _oHttpClient = null;
        HttpPost _oHttpRequest = null;
        String _sContentnt = null; //$NON-NLS-1$
        try {
            if (isSSL) {
                _oHttpClient = new SSLHttpClient();
            } else {
                _oHttpClient = new DefaultHttpClient();
            }
            // 设置等待数据超时时间，即socket超时时间
            _oHttpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, iTimeOut);
            // 设置请求超时时间，即连接超时时间：发起请求前的等待时间
            _oHttpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, iTimeOut);
            // 设置连接不够用的时候等待超时时间、不能太大， 如果这个参数没有被设置，默认等于CONNECTION_TIMEOUT
            // _oHttpClient.getParams().setParameter(ClientPNames.CONN_MANAGER_TIMEOUT,
            // 500L);
            // 在提交请求之前 测试连接是否可用
            // _oHttpClient.getParams().setParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK,
            // true);

            if (isNeedLog) {
                LogKit.info(sUrl);
            }
            _oHttpRequest = new HttpPost(sUrl);
            // 查看是否需要参数
            if (oPm != null && oPm.size() > 0) {
                _oHttpRequest.setEntity(new UrlEncodedFormEntity(oPm, charset));
                if (isNeedLog) {
                    LogKit.info(oPm.toString());
                }
            }
            // 设置等待数据超时时间，即socket超时时间
            _oHttpRequest.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, iTimeOut);
            // 设置请求超时时间，即连接超时时间：发起请求前的等待时间
            _oHttpRequest.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, iTimeOut);

            HttpResponse _oHttpResponse = _oHttpClient.execute(_oHttpRequest);
            int code = _oHttpResponse.getStatusLine().getStatusCode();
            // 非200 都是异常
            if (code != 200) {
                LogKit.error("[ERR_CODE][1001]" + code);
                if (code == 404) {
                    // 后台需要及时更新
                } else if (code == 504) {
                    // 门店网络超时
                }
                throw new Exception("Response Status Code :" + code); //$NON-NLS-1$
            }

            if (isNeedResponse) {
                HttpEntity _oHttpEntity = _oHttpResponse.getEntity();
                // 获取后台反馈的结果
                if (_oHttpEntity != null) {
                    _sContentnt = IOUtils.toString(_oHttpEntity.getContent(), charset); //$NON-NLS-1$
                }
                if (isNeedLog) {
                    LogKit.info("后台返回信息:" + _sContentnt);
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (_oHttpRequest != null) {
                _oHttpRequest.abort();
                _oHttpRequest = null;
            }
            if (_oHttpClient != null) {
                _oHttpClient.getConnectionManager().shutdown();
                _oHttpClient = null;
            }
        }
        return emisUtils.parseString(_sContentnt).trim();
    }

    @Override
    public String doGet(String sUrl, String sParameter) throws Exception {
        if (sUrl == null || "".equals(sUrl.trim())) { //$NON-NLS-1$
            LogKit.error("url is null");
            return "";
        }

        HttpClient _oHttpClient = null;
        HttpGet _oHttpGet = null;
        String _sContent = null;
        try {
            _oHttpClient = new DefaultHttpClient();
            // 设置等待数据超时时间，即socket超时时间
            _oHttpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, iTimeOut);
            // 设置请求超时时间，即连接超时时间：发起请求前的等待时间
            _oHttpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, iTimeOut);
            // 设置连接不够用的时候等待超时时间、不能太大， 如果这个参数没有被设置，默认等于CONNECTION_TIMEOUT
            // _oHttpClient.getParams().setParameter(ClientPNames.CONN_MANAGER_TIMEOUT,
            // 500L);
            // 在提交请求之前 测试连接是否可用
            // _oHttpClient.getParams().setParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK,
            // true);

            String _sUrl = sUrl + emisUtils.parseString(sParameter);
            LogKit.info(_sUrl);
            _oHttpGet = new HttpGet(_sUrl);
            // 设置等待数据超时时间，即socket超时时间
            _oHttpGet.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, iTimeOut);
            // 设置请求超时时间，即连接超时时间：发起请求前的等待时间
            _oHttpGet.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, iTimeOut);
            // 在提交请求之前 测试连接是否可用
            // _oHttpGet.getParams().setParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK,
            // true);

            HttpResponse _oHttpResponse = _oHttpClient.execute(_oHttpGet);
            int code = _oHttpResponse.getStatusLine().getStatusCode();
            // 非200 都是异常
            if (code != 200) {
                LogKit.error("[ERR_CODE][1001]" + code);
                if (code == 404) {
                    // 后台需要及时更新
                } else if (code == 504) {
                    // 门店网络超时
                }
                throw new Exception("Response Status Code :" + code); //$NON-NLS-1$
            }
            if (isNeedResponse) {
                HttpEntity _oHttpEntity = _oHttpResponse.getEntity();
                if (_oHttpEntity != null) {
                    _sContent = IOUtils.toString(_oHttpEntity.getContent(), charset); //$NON-NLS-1$
                }
                if (emisUtils.parseBoolean(requestMap.get("isHeaderDate"))) {
                    // 获取响应头部日期信息返回
                    responseMap.put("Date", _oHttpResponse.getFirstHeader("Date").getValue());
                }
                if (_sUrl.indexOf("RWIDTH=") < 0) { // 云端报表类查询返回信息过长不列出
                    LogKit.info("后台返回信息:" + _sContent);
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (_oHttpGet != null) {
                _oHttpGet.abort();
                _oHttpGet = null;
            }
            if (_oHttpClient != null) {
                _oHttpClient.getConnectionManager().shutdown();
                _oHttpClient = null;
            }
        }
        return emisUtils.parseString(_sContent).trim();
    }

    public static void main(String[] args) {
        try {
            HttpClientService _oHttpClient = new HttpClientImpl();
            _oHttpClient.doGet("http://www.urpos.net:8035/vi_bk/jsp/ccr/check_alive.jsp", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
