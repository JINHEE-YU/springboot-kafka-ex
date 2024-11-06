package com.example.springboot_kafka_ex;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot_kafka_ex.producer.KafkaProducer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class KafkaExAPIController {

  private final KafkaProducer kafkaProducer;

  @GetMapping("/send")
  public String sendMessage() {

    String topic = "topic1";
    String message = "Hello, Kafka!!";

    try {
      kafkaProducer.sendMessage(topic, message);
    } catch (Exception e) {
      log.error("오류발생", e);
    }
    return "hello";
  }

  @GetMapping("/send/msg")
  public String sendMessage(@RequestParam(value = "message") String message) {

    String topic = "topic1";

    try {
      kafkaProducer.sendMessage(topic, message);
      return "success";
    } catch (Exception e) {
      log.error("오류발생", e);
      return "failure";
    }
  }

  @GetMapping("/send2")
  public String sendMessage2() {

    String topic = "topic2";

    UserDTO user = UserDTO.builder().age(10).nickName("Mika").chatMsg("I'm Mika").build();
    try {
      kafkaProducer.sendMessage(topic, user);
      return "success";
    } catch (Exception e) {
      log.error("오류발생", e);
      return "failure";
    }
  }

  @PostMapping("/send2/user")
  public String sendUserDTO(@RequestBody UserDTO user) {

    String topic = "topic2";

    try {
      kafkaProducer.sendMessage(topic, user);
    } catch (Exception e) {
      log.error("오류발생", e);
    }
    return "hello";
  }

}
