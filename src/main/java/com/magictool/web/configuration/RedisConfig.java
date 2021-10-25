package com.magictool.web.configuration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * redis配置类
 * @author lijf
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setEnableTransactionSupport(true);
        //key和value的序列化机制
        //设置key的序列化机制为String的机制（直接存储值）
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //设置HashKey的序列化机制
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        //设置Value的序列化机制
        redisTemplate.setValueSerializer(getJsonSerializer());
        redisTemplate.setHashValueSerializer(getJsonSerializer());
        //设置RedisConnectionFactory
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }

    public Jackson2JsonRedisSerializer<Object> getJsonSerializer(){
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper om = new ObjectMapper();
        //只针对非空的属性进行序列化
//        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //访问类型
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //将类的全名序列化到json字符串中
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);
        //对于匹配不了的属性忽略报错信息
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //不包含任何属性的bean也不报错
        om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        serializer.setObjectMapper(om);
        return serializer;
    }

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(){
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig();
        return configuration.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(getJsonSerializer()))
                .entryTtl(Duration.ofMinutes(60));
    }


}
