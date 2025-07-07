package com.example.travel_project.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }
}
