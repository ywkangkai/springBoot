package com.weaver.accurate.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@ConfigurationProperties(prefix = "custom.link.start")
@Data
public class CustomizeLinkStartConfig {



    /**
     * 自定义链接类名
     */
    private List<String> classNameList;

    /**
     * 自定义链接方法签名
     */
    private List<String> methodSignList;
}
