package com.emis.vi.pay.verypay.utils.http;

import com.emis.vi.pay.verypay.config.VeryPayConfig;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

/**
 * $Id$
 * 2018/02/02 Francis.xie Modify 需求 #42028 [客制-宜芝多]费睿
 */
public class HttpClientUtil {

  public static String sendMsgHTTP(Map<String, Object> mPostMap, String sendURL) {
    String result = "";
    //协议默认使用--boundaryStr的格式来作为分界字符串
    //可以设为一个一般不会和数据冲突的随机字符串，这是用来区分多种数据的。
    String boundaryStr = "headrequestverypay";

    URL url = null;
    HttpURLConnection httpConn = null;
    OutputStream output = null;
    int returnCode = 0;
    try {
      url = new URL(sendURL);
      httpConn = (HttpURLConnection) url.openConnection();
      HttpURLConnection.setFollowRedirects(true);
      httpConn.setConnectTimeout(15000);
      httpConn.setReadTimeout(15000);
      httpConn.setDoInput(true); // 设置输入流采用字节流
      httpConn.setDoOutput(true);
      httpConn.setRequestMethod("POST");
      httpConn.setUseCaches(false); // Post 请求不能使用缓存
      httpConn.setInstanceFollowRedirects(true);
      httpConn.setRequestProperty("Accept", "application/json;"); // 设置接收数据的格式
      httpConn.setRequestProperty("Charset", "UTF-8"); // 设置字符编码类型
      httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); // 设置发送数据的格式
      //httpConn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundaryStr); // 设置发送数据的格式
      // 写入请求参数
      StringBuffer sb = new StringBuffer();
      Iterator<String> it = mPostMap.keySet().iterator();
      while (it.hasNext()) {
        String str = it.next();
        sb.append(str);
        sb.append("=");
        sb.append(mPostMap.get(str));
        sb.append("&");

        //----------multipart/form-data方式传输-----------
        //sb.append("--" + boundaryStr + "\r\n");
        //sb.append("Content-Disposition:form-data;name=\"");
        //sb.append(str);
        //sb.append("\"\r\n\r\n");
        //sb.append(mPostMap.get(str));
        //sb.append("\r\n");
      }
      sb.delete(sb.length() - 1, sb.length());
      if (VeryPayConfig.isDebug()) {
        VeryPayConfig.getLog().info("@@@" + sb.toString());
      }
      byte[] data = sb.toString().getBytes();
      httpConn.setRequestProperty("Content-Length", String.valueOf(data.length));
      //sb.append(("--" + boundaryStr + "--\r\n")); //在结尾的时候使用--boundaryStr--的格式作为结束标志
      output = httpConn.getOutputStream();
      output.write(data);
      httpConn.connect();
      returnCode = httpConn.getResponseCode();
    } catch (ConnectException e) {
      VeryPayConfig.getLog().error(e, e);
      return null;
    } catch (Exception e) {
      VeryPayConfig.getLog().error(e, e);
      return null;
    } finally {
      try {
        if (output != null) {
          output.flush();
          output.close();
          output = null;
        }
      } catch (Exception e) {
      }
    }
    if (VeryPayConfig.isDebug()) {
      VeryPayConfig.getLog().info("调用外系统返回码：" + returnCode);
    }
    if (returnCode == 200) {
      InputStream is = null;
      BufferedReader reader = null;
      try {
        is = httpConn.getInputStream();
        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        // 读取响应内容
        String readLine = "";
        while ((readLine = reader.readLine()) != null) {
          if (readLine.length() > 0)
            result = result + readLine.trim() + "\n";
        }
        if (VeryPayConfig.isDebug()) {
          VeryPayConfig.getLog().info("调用外系统返回报文：" + result);
        }
      } catch (IOException e) {
        VeryPayConfig.getLog().error(e, e);
        return null;
      } finally {
        try {
          if (reader != null) {
            reader.close();
          }
          if (is != null) {
            is.close();
          }
        } catch (IOException e) {
        }
        if (httpConn != null) {
          httpConn.disconnect();
          httpConn = null;
        }
      }
    }
    return result;
  }
}