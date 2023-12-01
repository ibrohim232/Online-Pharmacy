package com.example.onlinemedicine.config;

import com.example.onlinemedicine.dto.mail.EmailDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {
   @Bean
   public JedisConnectionFactory jedisConnectionFactory(){
      JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
      jedisConnectionFactory.setHostName("monorail.proxy.rlwy.net");
      jedisConnectionFactory.setDatabase(0);
      jedisConnectionFactory.setPassword("HppfO4opapGBcJH3J6LfJfFfIApB1bKG");
      jedisConnectionFactory.setPort(16690);
       return jedisConnectionFactory;
   }
   @Bean
    public RedisTemplate<String , EmailDto> redisTemplate(){
       RedisTemplate<String, EmailDto> template = new RedisTemplate<>();
       template.setKeySerializer(new StringRedisSerializer());
       template.setValueSerializer(new Jackson2JsonRedisSerializer<>(EmailDto.class));
       template.setConnectionFactory(jedisConnectionFactory());
       return template;
   }
}
