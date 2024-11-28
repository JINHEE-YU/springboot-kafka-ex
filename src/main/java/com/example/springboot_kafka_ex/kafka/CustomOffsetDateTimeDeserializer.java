package com.example.springboot_kafka_ex.kafka;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CustomOffsetDateTimeDeserializer {
}
// @Configuration
// public class CustomOffsetDateTimeDeserializer extends
// JsonDeserializer<OffsetDateTime> {
// @Override
// public OffsetDateTime deserialize(JsonParser p, DeserializationContext ctxt)
// throws IOException {
// String date = p.getText();
// return OffsetDateTime.parse(date, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
// }
// }