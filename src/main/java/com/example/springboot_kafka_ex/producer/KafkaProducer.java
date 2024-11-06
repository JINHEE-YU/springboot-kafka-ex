package com.example.springboot_kafka_ex.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.springboot_kafka_ex.UserDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final KafkaTemplate<String, UserDTO> kafkaTemplate2;

  public void sendMessage(String topic, String message) {
    log.info("call send message");
    kafkaTemplate.send(topic, message);
    log.info("Kafka Producer send data = {} / topic ={}", message, topic);
  }

  public void sendMessage(String topic, UserDTO message) {
    log.info("call send message");
    kafkaTemplate2.send(topic, message);
    log.info("Kafka Producer send data = {} / topic ={}", message, topic);
  }
}
