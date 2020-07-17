package com.emis.vi.bm;

import com.alipay.sofa.common.thread.log.ThreadLogger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@ImportResource({"classpath*:META-INF/vi-bm/*.xml"})
@SpringBootApplication
//@ServletComponentScan
public class ViBmApplication {

    // init the logger
    //private static final Logger logger = LoggerFactory.getLogger(ViBmApplication.class);
    //private static final Logger logger2 = LoggerFactory.getLogger("MDC-EXAMPLE");
    //private static final Logger sofaLogger = SofaLoggerFactory.getLogger("com.alipay.sofa.common.boot");

    public static void main(String[] args) {
        SpringApplication.run(ViBmApplication.class, args);
        //ThreadLogger.info("Thread pool with name '{}' registered");
        /*if (logger.isInfoEnabled()){
            logger.info("application start");
        }*/
        //logger2.info("rest mdc example");
        //sofaLogger.info("hello sofa-common-tools");
    }

    //Springboot对Jersey的配置方式二：返回一个ResourceConfig类型的@Bean
    /*@Bean
    public ResourceConfig resourceConfig() {
        ResourceConfig config = new ResourceConfig();
        config.register(SpringbootResource.class);
        return config;
    }*/

    //Springboot对Jersey的配置方式三：配置一组ResourceConfigCustomizer对象
    /*@Bean
    public ResourceConfig resourceConfig() {
        return new ResourceConfig();
    }*/
}
