package com.example.springboot_kafka_ex.kafka;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class OffsetDateTimeSerializer extends JsonSerializer<OffsetDateTime> {
  @Override
  public void serialize(OffsetDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    gen.writeString(value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
  }
}
// public class OffsetDateTimeSerializer<T> implements Serializer<T> {
// private final ObjectMapper objectMapper = JacksonConfig.objectMapper();

// @Override
// public void configure(Map<String, ?> configs, boolean isKey) {
// }

// @Override
// public byte[] serialize(String topic, T data) {
// try {
// return objectMapper.writeValueAsBytes(data);
// } catch (Exception e) {
// throw new RuntimeException("Error serializing value", e);
// }
// }

// @Override
// public void close() {
// }
// }