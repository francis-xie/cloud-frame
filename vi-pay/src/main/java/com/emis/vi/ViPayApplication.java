package com.emis.vi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * war方式部署，继承SpringBootServletInitializer
 */
@SpringBootApplication
@ServletComponentScan
public class ViPayApplication {
//public class ViPayApplication extends SpringBootServletInitializer { //war方式部署

    //war方式部署
    /*@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ViPayApplication.class);
    }*/
    public static void main(String[] args) {
        SpringApplication.run(ViPayApplication.class, args);
    }

}
