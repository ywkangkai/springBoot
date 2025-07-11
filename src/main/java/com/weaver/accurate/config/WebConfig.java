package com.weaver.accurate.config;//package com.example.auto_java.config;

import com.weaver.accurate.interceptor.Authenticationlnterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    Authenticationlnterceptor authenticationlnterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加拦截器
        registry.addInterceptor(authenticationlnterceptor)
                .addPathPatterns("/**"); //拦截所有请求
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)     //允许跨越发送cookie
                .allowedOriginPatterns("*")  //允许所有域名进行跨域调用
                .allowedHeaders("*")    //放行全部原始头信息
                .allowedMethods("*");   //允许所有请求方法跨域调用
    }
}
