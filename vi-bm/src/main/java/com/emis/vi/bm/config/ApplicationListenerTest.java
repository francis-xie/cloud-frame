package com.emis.vi.bm.config;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * SpringBoot程序启动时执行初始化代码
 * 利用Spring的事件机制，ContextRefreshedEvent事件进行初始化操作，该事件是ApplicationContext初始化完成后调用的事件
 */
@Component
public class ApplicationListenerTest implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //对应实现一个监听器，在其onApplicationEvent()方法里初始化操作
        //event.getApplicationContext().getBean(IPermissionService.class)；调用bean执行
        System.out.println("ApplicationListener：我被调用了..");
    }
}
