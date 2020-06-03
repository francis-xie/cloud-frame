package com.emis.vi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 应用启动入口
 */
@EnableDiscoveryClient
@SpringBootApplication
public class ViAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(ViAdminApplication.class, args);
    }

}
