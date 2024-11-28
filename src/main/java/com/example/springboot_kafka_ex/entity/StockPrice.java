package com.example.springboot_kafka_ex.entity;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stock_prices", schema = "public")
public class StockPrice {
  @Id
  @Column(name = "time", nullable = false)
  private OffsetDateTime time = OffsetDateTime.now();

  @Column(name = "symbol", nullable = false)
  private String symbol;

  @Column(name = "price")
  private Double price;

  @Column(name = "volume")
  private Integer volume;

  // 문자열을 받아서 StockPrice 객체를 생성하는 정적 메서드
  public static OffsetDateTime fromString(String timeString) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
    OffsetDateTime time = OffsetDateTime.parse(timeString, formatter);
    return time;

  }

}
