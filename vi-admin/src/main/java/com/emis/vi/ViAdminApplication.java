package com.emis.vi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 启动后台管理服务vi-admin
 */
@EnableDiscoveryClient //注解表明这是一个Eureka客户端，启用服务注册功能
@SpringBootApplication
public class ViAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(ViAdminApplication.class, args);
    }

}
