package com.emis.vi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer //启用配置中心功能
@EnableDiscoveryClient
@SpringBootApplication
public class ViConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(ViConfigApplication.class, args);
    }

}
