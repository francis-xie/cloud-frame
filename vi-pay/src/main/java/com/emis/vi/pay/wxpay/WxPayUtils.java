package com.emis.vi.pay.wxpay;

import com.emis.vi.pay.util.emisPropUtil;
import com.tencent.common.MD5;
import com.thoughtworks.xstream.XStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

/**
 * $Id$
 * 微信支付-工具类
 */
public class WxPayUtils {

  /**
   * private的constructor無法被new
   */
  private WxPayUtils() {
  }

  /**
   * 系统参数emisprop('WXPAY_KEY')
   * 微信商户平台(pay.weixin.qq.com)-->账户中心-->账户设置-->API安全-->密钥设置
   */
  public static String getKey(ServletContext context_) {
    try {
      return emisPropUtil.get("WXPAY_KEY", "");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  /**
   * 获取ip地址
   * @param request
   * @return
   */
  public static String getIpAddr(HttpServletRequest request) {
    InetAddress addr = null;
    try {
      addr = InetAddress.getLocalHost();
    } catch (UnknownHostException e) {
      return request.getRemoteAddr();
    }
    byte[] ipAddr = addr.getAddress();
    String ipAddrStr = "";
    for (int i = 0; i < ipAddr.length; i++) {
      if (i > 0) {
        ipAddrStr += ".";
      }
      ipAddrStr += ipAddr[i] & 0xFF;
    }
    return ipAddrStr;
  }

  /**
   * 将从API返回的XML数据映射到Java对象
   * @param xml     xml格式字符串
   * @param tClass  Java对象.class
   * @return Java对象
   */
  public static Object fromXML(String xml, Class tClass) {
    //将从API返回的XML数据映射到Java对象
    XStream xStreamForResponseData = new XStream();
    xStreamForResponseData.alias("xml", tClass);
    xStreamForResponseData.ignoreUnknownElements();//暂时忽略掉一些新增的字段
    return xStreamForResponseData.fromXML(xml);
  }

  /**
   * 随机字符串：用的是UUID去中划线
   * @return
   */
  public static String getNonce_str() {
    return UUID.randomUUID().toString().replace("-", "");
  }


  /**
   * 微信获取签名公共方法
   * @param map  xml数据参数
   * @param sKey  微信商户平台密钥
   * @return  签名字符串
   */
  public static String getSign(Map<String, Object> map, String sKey) {
    String sign = "";

    ArrayList<String> list = new ArrayList<String>();
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      if (!"".equals(entry.getValue())) {
        list.add(entry.getKey() + "=" + entry.getValue() + "&");
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
    result += "key=" + sKey;
    //Util.log("Sign Before MD5:" + result);
    System.out.println("Sign Before MD5:" + result);
    sign = MD5.MD5Encode(result).toUpperCase();
    //Util.log("Sign Result:" + result);
    System.out.println("Sign Result:" + sign);

    return sign;
  }

  /**
   * 发送pos请求
   * @param sURL      请求地址
   * @param postData  参数
   * @return
   */
  public static String httpPost(String sURL, String postData) {
    String sReturn = "";

    //连接超时时间，默认10秒
    int socketTimeout = 10000;
    //传输超时时间，默认30秒
    int connectTimeout = 30000;
    //设置请求器的配置
    RequestConfig requestConfig;
    requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();

    HttpPost httpPost = new HttpPost(sURL);
    //得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
    StringEntity postEntity = new StringEntity(postData, "UTF-8");
    httpPost.addHeader("Content-Type", "text/xml");
    httpPost.setEntity(postEntity);
    httpPost.setConfig(requestConfig);

    CloseableHttpClient httpClient;
    httpClient = HttpClients.custom().build();
    try {
      HttpResponse response = httpClient.execute(httpPost);
      HttpEntity entity = response.getEntity();
      sReturn = EntityUtils.toString(entity, "UTF-8");
    } catch (Exception e) {
      e.printStackTrace();
//      if(oLogger_ != null) oLogger_.error(e,e);
    } finally {
      httpPost.abort();
    }

    return sReturn;
  }

}
