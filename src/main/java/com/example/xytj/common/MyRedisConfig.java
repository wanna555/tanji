package com.example.xytj.common;

import com.example.xytj.pojo.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

/**
 * @title MyRedisConfig
 * @Author: ZKY
 * @CreateTime: 2023-05-02  23:39
 * @Description: TODO
 */
@Configuration
public class MyRedisConfig {
    @Bean
    public RedisTemplate<Object, User> userRedisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<Object,User> template = new RedisTemplate<Object,User>();
        template.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer<User> serializer = new Jackson2JsonRedisSerializer<User>(User.class);
        template.setDefaultSerializer(serializer);
        return template;
    }
}
