package com.example.springboot_kafka_ex;

import java.util.List;

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

    String message = "[\"Hello\", \"Kafka!!\"]]";

    try {
      kafkaProducer.sendMessage(message);
    } catch (Exception e) {
      log.error("오류발생", e);
    }
    return "hello";
  }

  @GetMapping("/send/msg")
  public String sendMessage(@RequestParam(value = "message") String message) {
    // 여러개의 message를 보내면 ,로 구분
    try {
      kafkaProducer.sendMessage(message);
      return "success";
    } catch (Exception e) {
      log.error("오류발생", e);
      return "failure";
    }
  }

  @Value("${spring.kafka.topic.msg-filter}")
  String mgsListTopic;

  @GetMapping("/send/msgs")
  public String sendMessages(@RequestParam(value = "message") List<String> message) {
    // 여러개의 message를 보내면 List형태로 구분
    try {
      kafkaProducer.sendMessage(mgsListTopic, message);
      return "success";
    } catch (Exception e) {
      log.error("오류발생", e);
      return "failure";
    }
  }

  @GetMapping("/send2")
  public String sendMessage2() {

    UserDTO user = new UserDTO();
    user.setAge(10);
    user.setNickName("Mika");
    user.setChatMsg("I'm Mika");
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

  @PostMapping("/send2/users")
  public String sendUserDTO(@RequestBody List<UserDTO> users) {

    kafkaProducer.sendMessage(users);

    return "hello";
  }

}
