package com.zb.meeteat.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zb.meeteat.domain.matching.dto.MatchingRequestDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    return new LettuceConnectionFactory();
  }

  @Bean
  @Primary
  public RedisTemplate<String, String> redisTemplate(
      RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, String> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new StringRedisSerializer());
    return template;
  }

  @Bean
  public RedisTemplate<String, MatchingRequestDto> redisMatchingTemplate(
      RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, MatchingRequestDto> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);

    // ✅ ObjectMapper 직접 생성 후 생성자에서 주입
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    objectMapper.findAndRegisterModules(); // ✅ LocalDateTime 등 직렬화 가능하도록 설정

    // 🔥 setObjectMapper() 제거하고 생성자에서 바로 주입
    Jackson2JsonRedisSerializer<MatchingRequestDto> serializer =
        new Jackson2JsonRedisSerializer<>(objectMapper, MatchingRequestDto.class);

    // Key와 Value Serializer 설정
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(serializer);
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(serializer);

    template.afterPropertiesSet();
    return template;
  }

  @Bean
  public RedisTemplate<String, Object> redisTeamTemplate(RedisConnectionFactory factory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(factory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    return template;
  }
}
