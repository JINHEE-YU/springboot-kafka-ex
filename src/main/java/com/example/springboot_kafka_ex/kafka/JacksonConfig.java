package com.example.springboot_kafka_ex.kafka;

import java.time.OffsetDateTime;
import java.util.TimeZone;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class JacksonConfig {
  @Bean
  public static ObjectMapper objectMapper() {
    // ObjectMapper 설정
    ObjectMapper objectMapper = new ObjectMapper();
    // Java 8 날짜/시간 모듈 등록
    objectMapper.registerModule(new JavaTimeModule());
    // 타임스탬프로 표기됨(초단위 데이터)
    // objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); //Unix
    // 기본 시간대를 Asia/Seoul로 설정
    objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
    return objectMapper;
  }
}