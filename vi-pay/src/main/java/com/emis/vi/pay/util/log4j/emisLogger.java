/* $Id: emisLogger.java 71901 2010-02-24 04:44:31Z dana.gao $
 *
 * Copyright (c) EMIS Corp. All Rights Reserved.
 */
package com.emis.vi.pay.util.log4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

/**
 * 包裝Log4J的Logger, 以簡化Log檔的使用.
 */
//@Component
public class emisLogger {
	private static boolean bInit = false;
	private static Properties oProps_;
	/*@Autowired
	private static ResourceLoader resourceLoader;*/

	private static void initLog4j() throws Exception {
		if (!bInit) {
			if ("true".equals(System.getProperty("IS_START_RECHARGE"))) {
				oProps_ = getLog4jProperties("log4jRecharge.properties");
			} else {
				oProps_ = getLog4jProperties("log4j.properties");
			}
			PropertyConfigurator.configure(oProps_);
			
			bInit = true;
		}
	}
	/**
	 * 记录发票历程的log
	 * @return
	 */
	public static Log getInvoiceLog(){
		Log log = null;
		try {
			initLog4j();
			log = LogFactory.getLog("invoiceLogger");  
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		return log;
	}
	public static Logger getLog(Class clazz) {
		return getLog(clazz.getName());
	}

	public static Logger getLog(String sClassName) {
		Logger oLogger = null;
		try {
			initLog4j();
			oLogger = Logger.getLogger(sClassName);
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		return oLogger;
	}

	private static Properties getLog4jProperties(String sFileName)
			throws Exception {
		FileInputStream in = null;
		try {
			//1.使用org.springframework.core.io.ClassPathResource读取resources下文件方式，各种环境都能读取。（通用）
			Resource resource = new ClassPathResource(sFileName);
			//2.结合spring注解读取resources下文件方式，使用org.springframework.core.io.ResourceLoader;类的注解。（通用）
			//Resource resource = resourceLoader.getResource("classpath:resource.properties");
			in = new FileInputStream(resource.getFile());
			//in = new FileInputStream(sFileName);
			Properties _oProps = new Properties();
			_oProps.load(in);
			return _oProps;
		} catch (FileNotFoundException e) {
			System.err.println("emisLogger: File not exists " + e.getMessage());
		} catch (IOException e) {
			System.err.println("emisLogger: IO error " + e.getMessage());
		} finally {
			if (in != null)
				in.close();
		}
		return null;
	}

	public static void main(String[] argv) {
		try {
			Logger log1 = emisLogger.getLog(emisLogger.class.getName());
			log1.info("log1 1");

			Logger log2 = emisLogger.getLog(emisLogger.class);
			log1.info("log1 2");
			log2.info("log2 1");
			log2.info("log2 2");
			log1.info("log1 3");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}