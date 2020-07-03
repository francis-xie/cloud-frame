package com.emis.vi.pay.hzsmk.util.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClientUtil {

	public static String sendMsgHTTP(String sendMsg, String sendURL, String sendDataType) {
		URL url = null;
		String result = "";
		
			sendDataType = "application/json;Charset=UTF-8";
		
		HttpURLConnection httpConn = null;
		OutputStream output = null;
		OutputStreamWriter outr = null;
		int returnCode = 0;
		try {
			url = new URL(sendURL);
			httpConn = (HttpURLConnection) url.openConnection();
			HttpURLConnection.setFollowRedirects(true);
			httpConn.setConnectTimeout(30000);
			httpConn.setReadTimeout(30000);
			httpConn.setDoInput(true); // 设置输入流采用字节流
			httpConn.setDoOutput(true);
			httpConn.setRequestMethod("POST");
			httpConn.setRequestProperty("Accept", "application/json;"); // 设置接收数据的格式
			httpConn.setRequestProperty("Content-Type", sendDataType); // 设置发送数据的格式
			httpConn.connect();
			output = httpConn.getOutputStream();
			outr = new OutputStreamWriter(output, "utf-8");
			// 写入请求参数
			outr.write(sendMsg.toCharArray(), 0, sendMsg.length());
			outr.flush();
			outr.close();
			returnCode = httpConn.getResponseCode();

		} catch (ConnectException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
		System.out.println("调用外系统返回码：" + returnCode);
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
				System.out.println("调用外系统返回报文：" + result);
			} catch (IOException e) {
				e.printStackTrace();
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
			}
		}
		return result;
	}
}