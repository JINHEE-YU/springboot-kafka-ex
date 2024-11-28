package com.example.springboot_kafka_ex.kafka;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class OffsetDateTimeDeserializer extends JsonDeserializer<OffsetDateTime> {
  @Override
  public OffsetDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    return OffsetDateTime.parse(p.getText(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
  }
}
// public class OffsetDateTimeDeserializer<T> implements Deserializer<T> {
// private final ObjectMapper objectMapper = JacksonConfig.objectMapper();
// private Class<T> clazz;

// public OffsetDateTimeDeserializer(Class<T> clazz) {
// this.clazz = clazz;
// }

// @Override
// public void configure(Map<String, ?> configs, boolean isKey) {
// }

// @Override
// public T deserialize(String topic, byte[] data) {
// try {
// return objectMapper.readValue(data, clazz);
// } catch (Exception e) {
// throw new RuntimeException("Error deserializing value", e);
// }
// }

// @Override
// public void close() {
// }
// }