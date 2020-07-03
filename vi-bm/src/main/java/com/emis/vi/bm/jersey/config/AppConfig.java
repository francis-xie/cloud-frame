package com.emis.vi.bm.jersey.config;

import com.emis.vi.bm.jersey.service.IEmisService;
import com.emis.vi.bm.jersey.service.impl.emisBMSynDataImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean(name = "syndata_checkLogin")
    public IEmisService emisBMSynDataImpl() {
        return new emisBMSynDataImpl();
    }

}