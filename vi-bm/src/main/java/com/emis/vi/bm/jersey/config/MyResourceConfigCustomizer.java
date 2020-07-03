package com.emis.vi.bm.jersey.config;

import com.emis.vi.bm.jersey.resource.SpringbootResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.autoconfigure.jersey.ResourceConfigCustomizer;
import org.springframework.stereotype.Component;

/**
 * Springboot对Jersey的配置方式三：配置一组ResourceConfigCustomizer对象
 */
//@Component
public class MyResourceConfigCustomizer /*implements ResourceConfigCustomizer*/ {
    /*@Override
    public void customize(ResourceConfig config) {
        config.register(SpringbootResource.class);
    }*/
}