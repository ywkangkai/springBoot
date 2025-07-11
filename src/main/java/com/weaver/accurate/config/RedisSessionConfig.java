package com.weaver.accurate.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;


@Configuration
//分别设置会话有效时长、会话更新redis模式、命名空间
//@EnableRedisHttpSession(maxInactiveIntervalInSeconds= 120, flushMode = FlushMode.ON_SAVE, redisNamespace = "bug_record")
@EnableRedisHttpSession
public class RedisSessionConfig {

}
