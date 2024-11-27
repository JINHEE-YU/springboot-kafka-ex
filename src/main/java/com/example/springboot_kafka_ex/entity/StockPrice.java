package com.example.springboot_kafka_ex.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stock_prices", schema = "public")
public class StockPrice {
  @Id
  @Column(name = "time", nullable = false)
  private OffsetDateTime time;

  @Column(name = "symbol", nullable = false)
  private String symbol;

  @Column(name = "price")
  private Double price;

  @Column(name = "volume")
  private Integer volume;

  /**
   * <p>
   * '@PrePersist'로 정의된 함수
   * </p>
   * <ul>
   ** <li>StockPrices 엔티티가 DB에 저장되기 전, 자동으로 호출</li>
   ** <li>time에 현재 시간 자동 할당</li>
   * </ul>
   */
  @PrePersist
  public void prePersist() {
    if (this.time == null) {
      this.time = OffsetDateTime.now();
    }
  }
}
