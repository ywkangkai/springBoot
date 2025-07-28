package com.weaver.accurate.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


@Configuration
public class JsonConfig implements WebMvcConfigurer {



    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                //List字段如果为null,输出为[],而非null
                SerializerFeature.WriteNullListAsEmpty,
                //是否输出值为null的字段,默认为false
                SerializerFeature.WriteMapNullValue,
                //字符串null返回空字符串
                SerializerFeature.WriteNullStringAsEmpty,
                //空布尔值返回false
                SerializerFeature.WriteNullBooleanAsFalse,
                //禁止循环引用
                SerializerFeature.DisableCircularReferenceDetect,
                //结果是否格式化,默认为false
                SerializerFeature.PrettyFormat);

        //格式化日期
        fastJsonConfig.setDateFormat("YYYY-MM-dd HH:mm:ss");
        converter.setFastJsonConfig(fastJsonConfig);
        converters.add(0, converter);
    }


}
