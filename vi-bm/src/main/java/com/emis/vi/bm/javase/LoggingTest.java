package com.emis.vi.bm.javase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.LoggerFactory;

import java.util.logging.Logger;

/**

 */
public class LoggingTest {
    public static void main(String[] args) {
        testJdkLogger();
        testApacheLogger();
        testSlf4jLogger();
    }

    /**
     * Java标准库内置了日志包java.util.logging，对它的测试使用
     * https://www.liaoxuefeng.com/wiki/1252599548343744/1264738568571776
     * JDK的Logging定义了7个日志级别，从严重到普通：SEVERE、WARNING、INFO、CONFIG、FINE、FINER、FINEST
     * 默认级别是INFO，因此，INFO级别以下的日志，不会被打印出来。
     */
    public static void testJdkLogger() {
        Logger logger = Logger.getGlobal();
        logger.info("start process...");
        logger.warning("memory is running out...");
        logger.fine("ignored.");
        logger.severe("process will be terminated...");
    }

    /**
     * 和Java标准库提供的日志不同，Commons Logging是一个第三方日志库，它是由Apache创建的日志模块。
     * https://www.liaoxuefeng.com/wiki/1252599548343744/1264738932870688
     * Commons Logging的特色是，它可以挂接不同的日志系统，并通过配置文件指定挂接的日志系统。
     * 默认情况下，Commons Loggin自动搜索并使用Log4j，如果没有找到Log4j，再使用JDK Logging。
     * Commons Logging定义了6个日志级别：(默认级别是INFO)：FATAL、ERROR、WARNING、INFO、DEBUG、TRACE
     */
    public static void testApacheLogger() {
        //通过LogFactory获取Log类的实例； 使用Log实例的方法打日志
        Log log = LogFactory.getLog(LoggingTest.class);
        log.info("start...");
        log.warn("end.");
        log.debug("debug");
        log.error("error");
    }

    /**
     * 前面介绍了Commons Logging，可以作为“日志接口”来使用。而真正的“日志实现”可以使用Log4j。
     * https://www.liaoxuefeng.com/wiki/1252599548343744/1264739436350112
     * Log4j是一种非常流行的日志框架，最新版本是2.x。Log4j是一个组件化设计的日志系统
     * 当我们使用Log4j输出一条日志时，Log4j自动通过不同的Appender把同一条日志输出到不同的目的地。
     * 例如：console：输出到屏幕；file：输出到文件；socket：通过网络输出到远程计算机；jdbc：输出到数据库
     * 在输出日志的过程中，通过Filter来过滤哪些log需要被输出，哪些log不需要被输出。例如，仅输出ERROR级别的日志。
     * 最后，通过Layout来格式化日志信息，例如，自动添加日期、时间、方法名称等信息。
     * 使用Log4j的时候，我们把一个log4j2.xml的文件放到相应目录下就可以让Log4j读取配置文件并按照我们的配置来输出日志。
     * 如：E:\Java\IdeaProjects\cloud-frame\vi-bm\document\log4j2.xml
     * 并引入第三方库，如：commons-logging-1.2.jar、log4j-api-2.x.jar、log4j-core-2.x.jar、log4j-jcl-2.x.jar
     * 这里使用了Commons Logging+Log4j的方式输出日志，它们一个负责充当日志API，一个负责实现日志底层
     */
    public static void testLog4jLogger() {
        //通过LogFactory获取Log类的实例； 使用Log实例的方法打日志
        //org.apache.commons.logging.Log、org.apache.commons.logging.LogFactory
        Log log = LogFactory.getLog(LoggingTest.class);
        log.info("start...");
        log.warn("end.");
        log.debug("debug");
    }

    /**
     * 使用SLF4J和Logback：https://www.liaoxuefeng.com/wiki/1252599548343744/1264739155914176
     * SLF4J类似于Commons Logging，也是一个日志接口，而Logback类似于Log4j，是一个日志的实现。
     * 引入第三方库，如：slf4j-api-1.7.x.jar、logback-classic-1.2.x.jar、logback-core-1.2.x.jar
     * 配置一个Logback的配置文件，把logback.xml放到相应目录下
     * 如：E:\Java\IdeaProjects\cloud-frame\vi-bm\document\logback.xml
     */
    public static void testSlf4jLogger() {
        //通过LoggerFactory获取Logger类的实例； 使用Logger实例的方法打日志
        //org.slf4j.Logger、org.slf4j.LoggerFactory
        org.slf4j.Logger logger = LoggerFactory.getLogger(LoggingTest.class);
        logger.info("start {}...", "org.slf4j.Logger"); //在SLF4J中对拼字符串输出比较友好
        logger.warn("end {}.", "org.slf4j.Logger");
        logger.debug("debug");
        logger.error("error");
    }
}
