package com.emis.vi.bm;

import com.emis.vi.bm.jersey.resource.SpringbootResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
//@ServletComponentScan
public class ViBmApplication {

    public static void main(String[] args) {
        SpringApplication.run(ViBmApplication.class, args);
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
