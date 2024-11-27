package com.example.springboot_kafka_ex.entity.repository;

import java.time.OffsetDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboot_kafka_ex.entity.StockPrice;

public interface StockPriceRepository extends JpaRepository<StockPrice, OffsetDateTime> {

}
