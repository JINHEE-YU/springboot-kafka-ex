package com.example.springboot_kafka_ex;

import org.springframework.beans.factory.annotation.Value;
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

    String message = "Hello, Kafka!!";

    try {
      kafkaProducer.sendMessage(message);
    } catch (Exception e) {
      log.error("오류발생", e);
    }
    return "hello";
  }

  @GetMapping("/send/msg")
  public String sendMessage(@RequestParam(value = "message") String message) {

    try {
      kafkaProducer.sendMessage(message);
      return "success";
    } catch (Exception e) {
      log.error("오류발생", e);
      return "failure";
    }
  }

  @GetMapping("/send2")
  public String sendMessage2() {

    UserDTO user = UserDTO.builder().age(10).nickName("Mika").chatMsg("I'm Mika").build();
    try {
      kafkaProducer.sendMessage(user);
      return "success";
    } catch (Exception e) {
      log.error("오류발생", e);
      return "failure";
    }
  }

  @PostMapping("/send2/user")
  public String sendUserDTO(@RequestBody UserDTO user) {

    try {
      kafkaProducer.sendMessage(user);
    } catch (Exception e) {
      log.error("오류발생", e);
    }
    return "hello";
  }

}
