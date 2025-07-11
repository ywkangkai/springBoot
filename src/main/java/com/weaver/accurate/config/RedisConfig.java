package com.weaver.accurate.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Configuration
public class RedisConfig extends CachingConfigurerSupport {
    @Value("${spring.cache.redis.time-to-live}")
    Long  ttl;
    @Autowired
    CacheKeyProperties cacheKeyProperties;
    /**
     * 缓存管理器
     * @param redisTemplate
     * @return
     */
    @Bean
    public RedisCacheManager redisCacheManager(RedisTemplate redisTemplate) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisTemplate.getConnectionFactory());
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getValueSerializer()))
//                .disableCachingNullValues()         //不缓存空值
                .entryTtl(Duration.ofMillis(ttl));      //使用配置后，application.propertis中的配置失效，后动添加

        //动态Key的缓存
        Set<String> cacheNames =new HashSet<>();
        ConcurrentHashMap<String, RedisCacheConfiguration> cacheConfigs = new ConcurrentHashMap<>();
        for (Map.Entry<String, Duration> entry : cacheKeyProperties.getCacheConfigs().entrySet()) {
            cacheNames.add(entry.getKey());
            cacheConfigs.put(entry.getKey(), redisCacheConfiguration.entryTtl(entry.getValue()));
        }

        RedisCacheManager redisCacheManager = RedisCacheManager.builder(redisCacheWriter).cacheDefaults(redisCacheConfiguration).initialCacheNames(cacheNames).withInitialCacheConfigurations(cacheConfigs).build();
        return redisCacheManager;
    }

    /**
     * retemplate相关配置
     * @param factory
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        //设置连接工厂
        redisTemplate.setConnectionFactory(factory);

        //定义使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的二进制序列化方式）
        Jackson2JsonRedisSerializer jacksonSeial = new Jackson2JsonRedisSerializer(Object.class);
        //设置序列化的Mapper
        ObjectMapper objectMapper = new ObjectMapper();
        //设置要系列化的的属性和作用域范围
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 设置序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
//        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);   //过时方法
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance , ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);
        jacksonSeial.setObjectMapper(objectMapper);

        // 设置value使用json序列化反序列化
        redisTemplate.setValueSerializer(jacksonSeial);
        //设置key使用StringRedisSerializer来序列化和反序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        // 设置hash key 和value序列化模式
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(jacksonSeial);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
}