package com.example.springboot_kafka_ex.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.springboot_kafka_ex.UserDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

  @Value("${spring.kafka.topic.first}")
  private String topic1;
  @Value("${spring.kafka.topic.second}")
  private String topic2;

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final KafkaTemplate<String, UserDTO> kafkaTemplate2;

  public void sendMessage(String message) {
    sendMessage(kafkaTemplate, topic1, message);
  }

  public void sendMessage(UserDTO message) {
    sendMessage(kafkaTemplate2, topic2, message);
  }

  private <T> void sendMessage(KafkaTemplate<String, T> tpl, String topic, T message) {
    try {
      log.info("Sending message to topic: {}", topic);
      tpl.send(topic, message).whenComplete((result, ex) -> {
        if (ex != null) {
          log.error("Failed to send message = {} to topic = {} due to {}", message, topic, ex.getMessage());
        } else {
          log.info("Kafka Producer sent data = {} to topic = {}", message, topic);
        }
      });
    } catch (Exception e) {
      log.error("Exception while sending message = {} to topic = {}", message, topic, e);
    }
  }
}
