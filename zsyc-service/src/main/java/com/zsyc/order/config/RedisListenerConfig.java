package com.zsyc.order.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.integration.redis.util.RedisLockRegistry;

import javax.annotation.Resource;

/**
 * @program: zsyc-parent
 * @description: 定义配置RedisListenerConfig
 * @author: Mr.Ning
 * @create: 2019-02-27 10:01
 **/

@Configuration
public class RedisListenerConfig {

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 处理乱码
     * @return
     */
    @Bean
    public RedisTemplate redisTemplateInit() {
        // key序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //val实例化
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return redisTemplate;
    }

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }

    @Bean
    public RedisLockRegistry redisLockRegistry(RedisConnectionFactory connectionFactory) {
        return new RedisLockRegistry(connectionFactory, "ZSYC-API");
    }
}
