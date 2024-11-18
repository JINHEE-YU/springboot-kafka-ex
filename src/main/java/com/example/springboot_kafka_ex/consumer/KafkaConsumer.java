package com.example.springboot_kafka_ex.consumer;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

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

  /** 콤마(,)를 기준으로 구분된 1건의 "문자열" 데이터 수령 */
  @KafkaListener(topics = "${spring.kafka.topic.first}")
  public void listen(String message) {
    log.info("kafka message = {}", message);
  }

  /**
   * "List 형태의" 단일 데이터 수령. 데이터를 간편하게 필터링하여 추출할 수 있음
   * 이처럼 filterBatch를 적용하는 경우
   * containerFactory에서 호출하게되는 RecordFilterStrategy 사용을 피해야 한다.
   */
  @KafkaListener(topics = "${spring.kafka.topic.msg-filter}", containerFactory = "kafkaListenerStrListContainerFactory")
  public void listen(List<String> messages) {
    log.info("kafka message = {}", messages);

    // 메시지 값이 "추출"로 시작하는 데이터 필터
    List<String> filteredRecords = messages.stream()
        .filter(msg -> msg.startsWith("추출"))
        .collect(Collectors.toList());

    for (String msg : filteredRecords) {
      log.info("Filtered Message: " + msg);
    }
  }

  @KafkaListener(topics = "${spring.kafka.topic.second}", containerFactory = "filterListenerContainerFactory")
  public void listen(ConsumerRecord<String, UserDTO> message) {
    log.info("kafka message = {}", message);
  }

  @KafkaListener(topics = "${spring.kafka.topic.dto-list}", containerFactory = "kafkaListenerUserListContainerFactory")
  public void listenUserList(List<UserDTO> message) {
    log.info("kafka message = {}", message);
  }

  @KafkaListener(topics = "${spring.kafka.topic.test}")
  public void listenToTest(ConsumerRecord<?, ?> consumerRecord) {
    setPayload(consumerRecord.toString());
    latch.countDown();
  }

}