package com.example.springboot_kafka_ex;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

  private final KafkaTemplate<String, String> kafkaTemplate;

  public void sendMessage(String topic, String message) {
    log.info("call send message");
    kafkaTemplate.send(topic, message);
    log.info("Kafka Producer send data = {} / topic ={}", message, topic);
  }
}
