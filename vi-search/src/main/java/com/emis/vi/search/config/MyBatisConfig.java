package com.emis.vi.search.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis配置类
 */
@Configuration
@MapperScan({"com.emis.vi.mapper","com.emis.vi.search.dao"})
public class MyBatisConfig {
}
