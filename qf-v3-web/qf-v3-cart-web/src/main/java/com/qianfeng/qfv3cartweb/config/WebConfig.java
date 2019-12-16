package com.qianfeng.qfv3cartweb.config;

import com.qianfeng.qfv3cartweb.intercepter.AuthIntercepter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author CYY
 * @date 2019/10/31 0031 11:24
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Resource
    private AuthIntercepter authIntercepter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /**
         * 添加拦截器并对所有路径进行拦截
         */
        registry.addInterceptor(authIntercepter).addPathPatterns("/**");
    }
}
