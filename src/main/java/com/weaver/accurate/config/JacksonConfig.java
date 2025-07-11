package com.weaver.accurate.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//作用：ResponseData返回时，当key对应的value为null时，会自动化去掉该字段不进行返回
//使用前{'code': 200, 'message': '任务名称已存在', total:null, 'data': null, 'pageSize': null, 'pageNum': null}
//使用后 {'code': 200, 'message': '任务名称已存在'}
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS); // 保留所有字段

        // 定义自定义过滤器
        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.addFilter("customFilter", new SimpleBeanPropertyFilter() {
            @Override
            public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer) throws Exception {
                // 仅当字段为 total, pageSize, pageNum 且值为 null 时跳过序列化
                if (writer.getName().equals("total") || writer.getName().equals("pageSize") || writer.getName().equals("pageNum")) {
                    Object value = writer.getMember().getValue(pojo);
                    if (value == null) {
                        return; // 跳过序列化
                    }
                }
                writer.serializeAsField(pojo, jgen, provider); // 正常序列化其他字段
            }
        });

        objectMapper.setFilterProvider(filterProvider);
        objectMapper.addMixIn(Object.class, CustomFilterMixin.class); // 应用过滤器
        return objectMapper;
    }

    // 定义一个 Mixin 类，用于绑定过滤器
    @JsonInclude(JsonInclude.Include.ALWAYS)
    @com.fasterxml.jackson.annotation.JsonFilter("customFilter")
    public static class CustomFilterMixin {
    }
}
