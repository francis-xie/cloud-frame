package com.emis.vi.pay.jwt.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis配置类
 */
@Configuration
@MapperScan({"com.emis.vi.pay.jwt.mapper"})
public class MyBatisConfig {
}
