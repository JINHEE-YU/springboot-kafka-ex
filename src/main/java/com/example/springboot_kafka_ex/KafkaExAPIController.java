package com.example.springboot_kafka_ex;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class KafkaExAPIController {

  private final KafkaProducer kafkaProducer;
  private static final String TOPIC = "topic1";
  private static final String MESSAGE = "Hello, Kafka!!";

  @GetMapping("/send")
  public String sendMessage() {

    try {
      kafkaProducer.sendMessage(TOPIC, MESSAGE);
    } catch (Exception e) {
      log.error("오류발생", e);
    }
    return "hello";
  }

}
