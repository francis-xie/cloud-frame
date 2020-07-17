package com.emis.vi.bm.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * 配置拦截器
 */
@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {
    /**
     * 登录拦截，对所有请求方法中有@LoginRequired 注解的进行拦截token认证处理
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor())
                .addPathPatterns("/**");    // 拦截所有请求，通过判断是否有 @LoginRequired 注解 决定是否需要登录
        super.addInterceptors(registry);
    }

    /**
     * 配置参数解析器，对参数中含有@CurrentUser注解的进行解析注入User对象
     * @param argumentResolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(currentUserMethodArgumentResolver());
        super.addArgumentResolvers(argumentResolvers);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(fastJsonHttpMessageConverterEx());
        super.configureMessageConverters(converters);
    }

    @Bean
    public FastJsonHttpMessageConverterEx fastJsonHttpMessageConverterEx() {
        return new FastJsonHttpMessageConverterEx();
    }

    /**
     * 参数解析器，对应@CurrentUser注解
     * @return
     */
    @Bean
    public CurrentUserMethodArgumentResolver currentUserMethodArgumentResolver() {
        return new CurrentUserMethodArgumentResolver();
    }

    /**
     * 登录拦截器，对应@LoginRequired注解
     * @return
     */
    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }
}
