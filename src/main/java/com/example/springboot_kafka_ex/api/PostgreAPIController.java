package com.example.springboot_kafka_ex.api;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot_kafka_ex.entity.Person;
import com.example.springboot_kafka_ex.entity.StockPrice;
import com.example.springboot_kafka_ex.entity.repository.PersonRepository;
import com.example.springboot_kafka_ex.entity.repository.StockPriceRepository;
import com.example.springboot_kafka_ex.kafka.producer.KafkaProducer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/jpa")
@RequiredArgsConstructor
public class PostgreAPIController {
  @Autowired
  private PersonRepository personRepository;
  @Autowired
  private StockPriceRepository stockPriceRepository;

  private final KafkaProducer kafkaProducer;

  @GetMapping("/person")
  public List<Person> getAllPersons() {
    return personRepository.findAll();
  }

  @PostMapping("/person")
  public Person createPerson(@RequestBody Person person) {
    return personRepository.save(person);
  }

  @GetMapping("/stock")
  public List<StockPrice> getAllStock() {
    return stockPriceRepository.findAll();
  }

  @PostMapping("/stock")
  public int createStockPrice(@RequestBody List<StockPrice> stockPrices) {
    // return stockPriceRepository.saveAll(stockPrices);// 키(저장시간) 중복 오류 발생

    int count = 0;
    StockPrice sp = null;
    for (StockPrice stockPrice : stockPrices) {

      stockPriceRepository.save(stockPrice);

      if (Objects.nonNull(sp)) {
        count++;
        sp = null;
      }
    }
    return count;

  }

  @PostMapping("/stock/send")
  public CompletableFuture<Boolean> sendStockPrice(@RequestBody List<StockPrice> stockPrices) {

    CompletableFuture<Boolean> future = kafkaProducer.send(stockPrices);

    log.info("try sending : {}", stockPrices.toString());

    return future.handle((result, ex) -> {
      if (Objects.nonNull(ex)) {
        ex.printStackTrace();
        log.error("message send error : " + ex.getMessage());
      }
      return result;
    });

  }

  @PostMapping("/stock/send/test")
  public CompletableFuture<Boolean> sendStockPriceTest(@RequestBody List<StockPrice> body) {

    List<StockPrice> stockPrices = new ArrayList<StockPrice>();
    StockPrice stockPrice = new StockPrice();
    stockPrice.setTime(OffsetDateTime.parse("2024-11-28T23:51:00+09:00"));
    stockPrice.setSymbol("API");
    stockPrice.setPrice(4000.0);
    stockPrice.setVolume(40);
    stockPrices.add(stockPrice);

    CompletableFuture<Boolean> future = kafkaProducer.send(stockPrices);

    log.info("try sending : {}", stockPrices.toString());

    return future.handle((result, ex) -> {
      if (Objects.nonNull(ex)) {
        ex.printStackTrace();
        log.error("message send error : " + ex.getMessage());
      }
      return result;
    });

  }
}
