package com.emis.vi.bm.jersey.config;

import com.emis.vi.bm.jersey.service.IEmisService;
import com.emis.vi.bm.jersey.service.common.emisBMCommonDatasImpl;
import com.emis.vi.bm.jersey.service.syndata.emisBMSynDataImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Java Config配置，通过@Configuration告诉Spring，本类是一个配置类，请扫描本类中的bean
 */
@Configuration
//@Profile("bm")
public class AppConfig {

    /**
     * 公共资料(系统参数)查询
     *
     * @return
     */
    //将返回的对象将交由Spring容器进行管理
    @Bean(name = "common_prop")
    public IEmisService prop() {
        emisBMCommonDatasImpl obj = new emisBMCommonDatasImpl();
        obj.setDefaultAct("commonProp");
        return obj;
    }

    /**
     * 1.91 登录身份检查
     *
     * @return
     */
    //将返回的对象将交由Spring容器进行管理
    @Bean(name = "syndata_checkLogin")
    public IEmisService checkLogin() {
        //return new emisBMSynDataImpl();
        emisBMSynDataImpl obj = new emisBMSynDataImpl();
        obj.setDefaultAct("checkLogin");
        return obj;
    }

    /**
     * 1.90 同步数据
     *
     * @return
     */
    @Bean(name = "syndata_synPartData")
    public IEmisService synPartData() {
        //return new emisBMSynDataImpl();
        emisBMSynDataImpl obj = new emisBMSynDataImpl();
        obj.setDefaultAct("synPartData");
        return obj;
    }

}