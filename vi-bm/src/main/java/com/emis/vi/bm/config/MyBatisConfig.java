package com.emis.vi.bm.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MyBatis配置类
 */
@Configuration
@EnableTransactionManagement
@MapperScan({"com.emis.vi.bm.mapper", "com.emis.vi.bm.jwt.mapper"})
public class MyBatisConfig {
}
