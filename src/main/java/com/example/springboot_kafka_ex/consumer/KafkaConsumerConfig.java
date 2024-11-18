package com.example.springboot_kafka_ex.consumer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.example.springboot_kafka_ex.UserDTO;

@Configuration
public class KafkaConsumerConfig {
  @Bean
  public ConsumerFactory<String, String> consumerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "my-group");
    configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

    return new DefaultKafkaConsumerFactory<>(configProps);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    return factory;
  }

  @Bean
  public ConsumerFactory<String, List<String>> consumerStrListFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "my-group");
    configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

    configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class.getName());
    configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());

    configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, List.class.getName());
    configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "com.example.springboot_kafka_ex");

    return new DefaultKafkaConsumerFactory<>(configProps);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, List<String>> kafkaListenerStrListContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, List<String>> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerStrListFactory());
    return factory;
  }

  @Bean
  public ConsumerFactory<String, UserDTO> consumerUserFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "my-group");
    configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

    // 들어오는 record 를 객체로 받기 위한 deserializer
    JsonDeserializer<UserDTO> deserializer = new JsonDeserializer<>(UserDTO.class, false);

    return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), deserializer);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, UserDTO> filterListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, UserDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerUserFactory());
    factory.setRecordFilterStrategy(
        record -> record.value().getAge() > 30);
    return factory;
  }

  @Bean
  public ConsumerFactory<String, List<UserDTO>> consumerUserListFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "my-group");
    configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

    configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class.getName());
    configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
    configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, List.class.getName());
    configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "com.example.springboot_kafka_ex");

    return new DefaultKafkaConsumerFactory<>(configProps);

  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, List<UserDTO>> kafkaListenerUserListContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, List<UserDTO>> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerUserListFactory());
    return factory;
  }

}
