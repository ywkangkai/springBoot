package com.example.travel_project.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "mycache")
@Component
public class CacheKeyProperties {
    private final Map<String, Duration> cacheConfigs = new HashMap<>();

    public Map<String, Duration> getCacheConfigs() {
        Map<String, Duration> result =  cacheConfigs;
        return result;
    }
}