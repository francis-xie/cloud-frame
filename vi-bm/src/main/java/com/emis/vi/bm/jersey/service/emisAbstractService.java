package com.emis.vi.bm.jersey.service;

import org.glassfish.jersey.server.ContainerRequest;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;
import java.sql.SQLException;

/**
 * 微信会员服务抽象类
 * 2016/11/11 francis.xie modify 需求 #36444 [外卖平台对接]-饿了么外卖接口
 */
@Service
public abstract class emisAbstractService implements IEmisService {
  protected ServletContext context_ = null;
  protected Request request_ = null;
  protected HttpServletRequest httpServletRequest_ = null;

  public void setRequest(Request request) {
    this.request_ = request;
  }

  public void setHttpServletRequest(HttpServletRequest request) {
    this.httpServletRequest_ = request;
  }

  public void setServletContext(ServletContext context) {
    this.context_ = context;
  }

  /**
   * 处理Request请求（默认转码）
   *
   * @return
   */
  protected MultivaluedMap<String, String> parseRequest() {
    return parseRequest(true);
  }

  /**
   * 处理Request请求
   *
   * @param decode 是否转码
   * @return
   */
  protected MultivaluedMap<String, String> parseRequest(boolean decode) {
//    if ("POST".equals(request_.getMethod())) {
//      return ((ContainerRequest) request_).getFormParameters();
//    } else {
//      return ((ContainerRequest) request_).getQueryParameters(decode);
//    }
    return null;
  }
  public String doAction() throws Exception {
    String sRet = "";
    try {
      System.out.println("-----------Service Running");

      // 实作中继承并override该Method
      sRet = postAction();
      System.out.println(sRet);

      System.out.println("-----------Service End");
    } catch (Exception e) {
    } finally {
    }
    return sRet;
  }

  protected abstract String postAction() throws SQLException, Exception;
}
