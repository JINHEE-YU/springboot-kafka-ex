package com.example.springboot_kafka_ex;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {

  @KafkaListener(topics = "topic1")
  public void listen(String message) {
    log.info("kafka message = {}", message);
  }
}