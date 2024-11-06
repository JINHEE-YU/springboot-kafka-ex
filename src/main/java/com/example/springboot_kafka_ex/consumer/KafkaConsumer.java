package com.example.springboot_kafka_ex.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.springboot_kafka_ex.UserDTO;

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

  @KafkaListener(topics = "topic2", containerFactory = "filterListenerContainerFactory")
  public void listen(ConsumerRecord<String, UserDTO> message) {
    log.info("kafka message = {}", message);
  }
}