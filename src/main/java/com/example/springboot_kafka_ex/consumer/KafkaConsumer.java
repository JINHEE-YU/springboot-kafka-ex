package com.example.springboot_kafka_ex.consumer;

import java.util.concurrent.CountDownLatch;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.springboot_kafka_ex.UserDTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
public class KafkaConsumer {

  private CountDownLatch latch = new CountDownLatch(1);
  private String payload = null;

  @KafkaListener(topics = "${spring.kafka.topic.first}")
  public void listen(String message) {
    log.info("kafka message = {}", message);
  }

  @KafkaListener(topics = "${spring.kafka.topic.second}", containerFactory = "filterListenerContainerFactory")
  public void listen(ConsumerRecord<String, UserDTO> message) {
    log.info("kafka message = {}", message);
  }

  @KafkaListener(topics = "${spring.kafka.topic.test}")
  public void listenToTest(ConsumerRecord<?, ?> consumerRecord) {
    setPayload(consumerRecord.toString());
    latch.countDown();
  }

}