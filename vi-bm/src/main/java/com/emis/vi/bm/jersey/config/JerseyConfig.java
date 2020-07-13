package com.emis.vi.bm.jersey.config;

import com.emis.vi.bm.jersey.resource.SpringbootResource;
import com.emis.vi.bm.jersey.resource.SpringbootResource2;
import com.emis.vi.bm.jersey.resource.emisWebServiceEntry;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

/**
 * Springboot对Jersey的配置方式一：创建一个自定义的ResourceConfig
 * Springboot默认把Jersey的根路径映射在/*上
 * 如访问SpringbootResource可通过：http://localhost:8080/bm/resource/sayhi?msg=wolfcode
 * 可以通过添加@ApplicationPath注解把Jersey的根路径映射在/ws上
 * 如访问SpringbootResource可通过：http://localhost:8080/bm/ws/resource/sayhi?msg=wolfcode
 */
//@ApplicationPath("ws")
//@Component
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(SpringbootResource.class);
        register(SpringbootResource2.class);
        register(emisWebServiceEntry.class);
    }
}