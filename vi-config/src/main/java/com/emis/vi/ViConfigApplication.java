package com.emis.vi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer //注解启用配置中心功能
@EnableDiscoveryClient //注解启用Eureka客户端
@SpringBootApplication
public class ViConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(ViConfigApplication.class, args);
    }

}
