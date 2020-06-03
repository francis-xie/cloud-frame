package com.emis.vi;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@EnableAdminServer //启用admin-server功能
@SpringBootApplication
public class ViMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ViMonitorApplication.class, args);
    }

}
