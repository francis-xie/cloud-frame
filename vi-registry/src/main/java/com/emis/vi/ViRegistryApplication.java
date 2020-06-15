package com.emis.vi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 启动注册中心vi-registory
 */
@EnableEurekaServer //启用Euerka注册中心功能
@SpringBootApplication
public class ViRegistryApplication {

    public static void main(String[] args) {
        SpringApplication.run(ViRegistryApplication.class, args);
    }

}
