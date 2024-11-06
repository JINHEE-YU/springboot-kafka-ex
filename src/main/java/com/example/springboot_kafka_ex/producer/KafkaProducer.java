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
    sendMessage(kafkaTemplate, topic, message);
  }

  public void sendMessage(String topic, UserDTO message) {
    sendMessage(kafkaTemplate2, topic, message);
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
