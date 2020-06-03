package com.emis.vi.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.emis.vi")
public class ViPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(ViPortalApplication.class, args);
	}

}
