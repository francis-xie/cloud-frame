/*

 * $Header: /repository/src3/src/com/emis/test/emisServletContext.java,v 1.1.1.1 2005/10/14 12:43:10 andy Exp $

 *

 * Copyright (c) EMIS Corp.

 */

package com.emis.vi.bm.thread.schedule;


import javax.servlet.*;
import javax.servlet.descriptor.JspConfigDescriptor;
import java.io.InputStream;
import java.net.URL;
import java.util.*;


public class emisServletContext implements ServletContext {

  Hashtable oInitParam_ = new Hashtable();

  Hashtable oAttribute_ = new Hashtable();

  

  public emisServletContext() {

    // do nothing

  }

  

  public void setInitParam(String sParam,String sValue) {

    oInitParam_.put(sParam,sValue);

  }

  

  public String getServerInfo() {

    return "emis ServletContext Emulate Context Object";

  }

  

  public int getMajorVersion() {

    return 1;

  }

  

  public int getMinorVersion() {

    return 0;

  }

  @Override
  public int getEffectiveMajorVersion() {
    return 0;
  }

  @Override
  public int getEffectiveMinorVersion() {
    return 0;
  }


  public String getInitParameter(String sParam) {

    return (String) oInitParam_.get(sParam);

  }

  

  public Enumeration getInitParameterNames() {

    return oInitParam_.keys();

  }

  @Override
  public boolean setInitParameter(String name, String value) {
    return false;
  }


  public ServletContext getContext(String parm1) {

    return this;

  }

  public String getContextPath() {
    return null;
  }


  public String getRealPath(String sParam) {

    return sParam;

  }

  

  public RequestDispatcher getRequestDispatcher(String parm1) {

    return null;

  }

  public RequestDispatcher getNamedDispatcher(String parm1) {

    return null;

  }

  public String getMimeType(String parm1) {

    throw new UnsupportedOperationException("Method getMimeType() not yet implemented.");

  }

  public Object getAttribute(String sParam) {

    return oAttribute_.get(sParam);

  }



  public Enumeration getAttributeNames() {

    return oAttribute_.keys();

  }



  public void setAttribute(String sParam,Object oObj) {

    oAttribute_.put(sParam,oObj);

  }



  public void removeAttribute(String sParam) {

    oAttribute_.remove(sParam);

  }



  public void log(String parm1) {

  }

  public void log(String parm1, Throwable parm2) {

  }

  public InputStream getResourceAsStream(String parm1) {

    throw new UnsupportedOperationException("Method getResourceAsStream() not yet implemented.");

  }

  public Servlet getServlet(String parm1) throws javax.servlet.ServletException {

    throw new UnsupportedOperationException("Method getServlet() not yet implemented.");

  }

  public Enumeration getServlets() {

    throw new UnsupportedOperationException("Method getServlets() not yet implemented.");

  }

  public Enumeration getServletNames() {

    throw new UnsupportedOperationException("Method getServletNames() not yet implemented.");

  }

  public void log(Exception parm1, String parm2) {

    throw new UnsupportedOperationException("Method log() not yet implemented.");

  }

  public String getServletContextName() {

    return "com.emis.test.emisServletContext";

  }

  @Override
  public ServletRegistration.Dynamic addServlet(String servletName, String className) {
    return null;
  }

  @Override
  public ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet) {
    return null;
  }

  @Override
  public ServletRegistration.Dynamic addServlet(String servletName, Class<? extends Servlet> servletClass) {
    return null;
  }

  @Override
  public ServletRegistration.Dynamic addJspFile(String jspName, String jspFile) {
    return null;
  }

  @Override
  public <T extends Servlet> T createServlet(Class<T> c) throws ServletException {
    return null;
  }

  @Override
  public ServletRegistration getServletRegistration(String servletName) {
    return null;
  }

  @Override
  public Map<String, ? extends ServletRegistration> getServletRegistrations() {
    return null;
  }

  @Override
  public FilterRegistration.Dynamic addFilter(String filterName, String className) {
    return null;
  }

  @Override
  public FilterRegistration.Dynamic addFilter(String filterName, Filter filter) {
    return null;
  }

  @Override
  public FilterRegistration.Dynamic addFilter(String filterName, Class<? extends Filter> filterClass) {
    return null;
  }

  @Override
  public <T extends Filter> T createFilter(Class<T> c) throws ServletException {
    return null;
  }

  @Override
  public FilterRegistration getFilterRegistration(String filterName) {
    return null;
  }

  @Override
  public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
    return null;
  }

  @Override
  public SessionCookieConfig getSessionCookieConfig() {
    return null;
  }

  @Override
  public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes) {

  }

  @Override
  public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
    return null;
  }

  @Override
  public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
    return null;
  }

  @Override
  public void addListener(String className) {

  }

  @Override
  public <T extends EventListener> void addListener(T t) {

  }

  @Override
  public void addListener(Class<? extends EventListener> listenerClass) {

  }

  @Override
  public <T extends EventListener> T createListener(Class<T> c) throws ServletException {
    return null;
  }

  @Override
  public JspConfigDescriptor getJspConfigDescriptor() {
    return null;
  }

  @Override
  public ClassLoader getClassLoader() {
    return null;
  }

  @Override
  public void declareRoles(String... roleNames) {

  }

  @Override
  public String getVirtualServerName() {
    return null;
  }

  @Override
  public int getSessionTimeout() {
    return 0;
  }

  @Override
  public void setSessionTimeout(int sessionTimeout) {

  }

  @Override
  public String getRequestCharacterEncoding() {
    return null;
  }

  @Override
  public void setRequestCharacterEncoding(String encoding) {

  }

  @Override
  public String getResponseCharacterEncoding() {
    return null;
  }

  @Override
  public void setResponseCharacterEncoding(String encoding) {

  }

  public Set getResourcePaths(String path) {

    return new HashSet();

  }

  public URL getResource(String parm1) throws java.net.MalformedURLException {

    throw new UnsupportedOperationException("Method getResource() not yet implemented.");

  }

}