package com.example.springboot_kafka_ex.kafka.producer;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.example.springboot_kafka_ex.entity.StockPrice;
import com.example.springboot_kafka_ex.kafka.UserDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

  @Value("${spring.kafka.topic.first}")
  private String topic1;
  @Value("${spring.kafka.topic.msg-filter}")
  private String topicMsgList;
  @Value("${spring.kafka.topic.second}")
  private String topic2;
  @Value("${spring.kafka.topic.dto-list}")
  private String topicDtoList;
  @Value("${spring.kafka.topic.stock-price}")
  private String topicStockPrice;
  /**
   * groupId를 공유하는 소비자 간에 메시지를 중복되지 않게 병렬로 처리
   * 현상 확인을 위해 3개의 파티션으로 구성
   */
  @Value("${spring.kafka.topic.group-test}")
  private String topicGroupTest;
  /**
   * Kafka에서는 key를 해시하여 파티션을 결정
   * 현상 확인을 위해 "check-partition"토픽의 파티션을 10개로 할당
   */
  @Value("${spring.kafka.topic.check-partition}")
  String checkPartitionTopic;

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final KafkaTemplate<String, List<String>> kafkaStringListTemplate;
  private final KafkaTemplate<String, UserDTO> kafkaUserTemplate;
  private final KafkaTemplate<String, List<UserDTO>> kafkaUserListTemplate;
  private final KafkaTemplate<String, List<StockPrice>> stockPriceListTemplate;

  public void sendMessage2CheckPartition(String messageKey, String message) {
    sendMessage(kafkaTemplate, checkPartitionTopic, messageKey, message);
  }

  public void sendMessage(String topic, String message) {
    sendMessage(kafkaTemplate, topic, message);
  }

  public void sendMessage(String message) {
    sendMessage(kafkaTemplate, topic1, message);
  }

  public void sendMessage4GroupIdTest(String message) {
    sendMessage(kafkaTemplate, topicGroupTest, message);
  }

  public void sendMessage(String topic, List<String> message) {
    sendMessage(kafkaStringListTemplate, topic, message);
  }

  public void sendMessage(String topic, UserDTO message) {
    sendMessage(kafkaUserTemplate, topic, message);
  }

  public void sendMessage(UserDTO message) {
    sendMessage(kafkaUserTemplate, topic2, message);
  }

  public void sendMessage(List<UserDTO> messages) {
    sendMessage(kafkaUserListTemplate, topicDtoList, messages);
  }

  public CompletableFuture<Boolean> send(List<StockPrice> stockPrices) {
    CompletableFuture<Boolean> future = sendStockPriceMessage(stockPriceListTemplate, topicStockPrice, stockPrices);

    return future.thenApply(isSuccess -> {
      log.info("Was the message sent successfully? : " + isSuccess);
      return isSuccess;
    });

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

  private <T> void sendMessage(KafkaTemplate<String, T> tpl, String topic, String msgKey, T message) {
    try {
      tpl.send(topic, msgKey, message).whenComplete((result, ex) -> {
        if (ex != null) {
          log.error("Failed to send message-key ={}, message = {} to topic = {} due to {}", msgKey, message, topic,
              ex.getMessage());
        } else {
          log.info("Kafka Producer sent message: " + message + " with key: " + msgKey + " to partition: "
              + result.getRecordMetadata().partition());

        }
      });
    } catch (Exception e) {
      log.error("Exception while sending message-key ={}, message = {} to topic = {}", msgKey, message, topic, e);
    }
  }

  private <T> CompletableFuture<Boolean> sendStockPriceMessage(KafkaTemplate<String, T> tpl, String topic, T message) {
    CompletableFuture<SendResult<String, T>> future = tpl.send(topic, message);

    return future.thenApply(sendResult -> {
      // 메시지 전송 성공
      log.info("Message sent successfully: " + sendResult.getRecordMetadata());
      log.info("topic = {}, message = {}", topic, message.toString());
      return true;
    }).exceptionally(ex -> {
      // 메시지 전송 실패
      log.error("Message sending failed: " + ex.getMessage());
      return false;
    });

  }
}
