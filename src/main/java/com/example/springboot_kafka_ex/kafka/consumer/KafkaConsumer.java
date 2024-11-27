package com.example.springboot_kafka_ex.kafka.consumer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.springboot_kafka_ex.entity.StockPrice;
import com.example.springboot_kafka_ex.entity.repository.StockPriceRepository;
import com.example.springboot_kafka_ex.kafka.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

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

  @Autowired
  private StockPriceRepository stockPriceRepository;

  /** 콤마(,)를 기준으로 구분된 1건의 "문자열" 데이터 수령 */
  @KafkaListener(topics = "${spring.kafka.topic.first}", groupId = "1")
  public void listen(String message) {
    log.info("[Group1A]kafka message = {}", message);
  }

  @KafkaListener(topics = "${spring.kafka.topic.group-test}", groupId = "1")
  public void listen1A(String message) {
    log.info("[Group1A]kafka message = {}", message);
  }

  @KafkaListener(topics = "${spring.kafka.topic.group-test}", groupId = "1")
  public void listen1B(String message) {
    log.info("[Group1B]kafka message = {}", message);
  }

  @KafkaListener(topics = "${spring.kafka.topic.group-test}", groupId = "2")
  public void listen2(String message) {
    log.info("[Group2]kafka message = {}", message);
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

  @KafkaListener(topics = "${spring.kafka.topic.stock-price}", containerFactory = "kafkaListenerStockPriceListContainerFactory")
  public void listenStockPriceList(List<StockPrice> messages) {
    log.info("kafka message = {}", messages.toString());

    ObjectMapper objectMapper = new ObjectMapper();

    try {
      for (Object linkedHashMap : messages) {
        if (linkedHashMap instanceof LinkedHashMap) {
          // LinkedHashMap을 StockPrice로 변환
          StockPrice stockPrice = objectMapper.convertValue(linkedHashMap, StockPrice.class);
          stockPriceRepository.save(stockPrice);
        } else {
          log.warn("Unexpected type: {}", linkedHashMap.getClass().getName());
        }
      }
    } catch (Exception e) {
      log.error("Error processing stock prices: {}", e.getMessage());
    }

  }

}