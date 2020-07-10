package com.emis.vi.bm.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 在项目启动时初始化一些操作，ApplicationRunner的执行时点是在SpringBoot应用的ApplicationContext完全初始化开始工作之后
 * springboot系列文章之启动时初始化数据：https://blog.csdn.net/pjmike233/article/details/81908540
 */
@Component
public class ApplicationRunnerTest implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("ApplicationRunner");
    }
}