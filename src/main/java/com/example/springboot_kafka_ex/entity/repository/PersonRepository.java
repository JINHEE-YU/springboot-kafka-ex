package com.example.springboot_kafka_ex.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springboot_kafka_ex.entity.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
