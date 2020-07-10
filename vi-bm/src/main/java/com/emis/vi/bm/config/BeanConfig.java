package com.emis.vi.bm.config;

import org.springframework.context.annotation.Configuration;

/**
 * Java Config配置，通过@Configuration告诉Spring，本类是一个配置类，请扫描本类中的bean
 */
@Configuration
public class BeanConfig {

    /*@Bean(name = "emisServer")
    public emisServer getEmisScheduleMgr() {
        emisServletContext servlet = new emisServletContext();
        emisServer Server_ = null;
        try {
            Server_ = new emisServerImpl(servlet, "E:\\Java\\IdeaProjects\\cloud-frame\\vi-bm\\src\\main\\resources\\bm.cfg", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Server_;
    }*/
}
