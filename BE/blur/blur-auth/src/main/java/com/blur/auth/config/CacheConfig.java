package com.blur.auth.config;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.RequiredArgsConstructor;
import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class CacheConfig {
	 
	private final RedisConnectionFactory connectionFactory;
	
	@Bean
	  public CacheManager cacheManager() {
	    RedisCacheConfiguration redisConfiguration = RedisCacheConfiguration.defaultCacheConfig()
	        .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
	        .serializeValuesWith(
	            RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
	        .entryTtl(Duration.ofSeconds(30));

	    return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(connectionFactory)
	        .cacheDefaults(redisConfiguration).build();
	  }
}
