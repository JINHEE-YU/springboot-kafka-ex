package com.example.springboot_kafka_ex;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import com.example.springboot_kafka_ex.entity.StockPrice;
import com.example.springboot_kafka_ex.kafka.JacksonConfig;
import com.example.springboot_kafka_ex.kafka.consumer.KafkaConsumer;
import com.example.springboot_kafka_ex.kafka.producer.KafkaProducer;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class SpringbootKafkaExApplicationTests {

	private static final String TEST_MESSAGE = "Test Kafka Message";

	@Autowired
	private KafkaConsumer consumer;

	@Autowired
	private KafkaProducer producer;

	@Value("${spring.kafka.topic.test}")
	private String topic;

	@Test
	public void sendAndGetMessage()
			throws Exception {
		producer.sendMessage(topic, TEST_MESSAGE);
		consumer.getLatch().await(1000, TimeUnit.MILLISECONDS);

		assertThat(consumer.getLatch().getCount(), equalTo(0L));
		assertThat(consumer.getPayload(), containsString(TEST_MESSAGE));
	}

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void testStockPriceSerialization1() throws Exception {
		// ObjectMapper 설정
		ObjectMapper objectMapper = JacksonConfig.objectMapper();// new ObjectMapper();
		// // Java 8 날짜/시간 모듈 등록
		// objectMapper.registerModule(new JavaTimeModule());
		// // 기본 시간대를 Asia/Seoul로 설정
		// objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

		// StockPrice 객체 생성
		StockPrice stockPrice = new StockPrice();
		stockPrice.setTime(OffsetDateTime.now(ZoneId.of("Asia/Seoul")));
		stockPrice.setSymbol("AAPL");
		stockPrice.setPrice(150.0);
		stockPrice.setVolume(1000);

		// 직렬화: StockPrice -> JSON
		String json = objectMapper.writeValueAsString(stockPrice);
		System.out.println("Serialized JSON: " + json);

		// 역직렬화: JSON -> StockPrice
		StockPrice deserializedStockPrice = objectMapper.readValue(json, StockPrice.class);
		System.out.println("Deserialized StockPrice: " + deserializedStockPrice);

		assertThat(deserializedStockPrice, equalTo(stockPrice));
	}

	@Test
	public void testStockPriceSerialization2() throws Exception {
		// ObjectMapper 설정
		ObjectMapper objectMapper = new ObjectMapper();
		// Java 8 날짜/시간 모듈 등록
		objectMapper.registerModule(new JavaTimeModule());
		// 직렬화 시 시간대 정보를 포함하도록 설정
		objectMapper.configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true);
		// 역직렬화 시 시간대 정보를 무시하지 않도록 설정
		objectMapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE,
				false);

		// StockPrice 객체 생성
		StockPrice stockPrice = new StockPrice();
		stockPrice.setTime(OffsetDateTime.now(ZoneId.of("Asia/Seoul")));
		stockPrice.setSymbol("AAPL");
		stockPrice.setPrice(150.0);
		stockPrice.setVolume(1000);

		// 직렬화: StockPrice -> JSON
		String json = objectMapper.writeValueAsString(stockPrice);
		System.out.println("Serialized JSON: " + json);

		// 역직렬화: JSON -> StockPrice
		StockPrice deserializedStockPrice = objectMapper.readValue(json, StockPrice.class);
		// 시간대를 수동으로 설정해야 함
		deserializedStockPrice
				.setTime(deserializedStockPrice.getTime().atZoneSameInstant(ZoneId.of("Asia/Seoul")).toOffsetDateTime());
		System.out.println("Deserialized StockPrice: " + deserializedStockPrice);

		assertThat(deserializedStockPrice, equalTo(stockPrice));
	}

	@Test
	public void testOffsetDateTimeSerialization() throws Exception {
		// 테스트할 OffsetDateTime 객체 생성
		OffsetDateTime now = OffsetDateTime.now();

		// 객체를 JSON으로 직렬화
		String json = objectMapper.writeValueAsString(now);

		// JSON에서 다시 OffsetDateTime으로 역직렬화
		OffsetDateTime deserialized = objectMapper.readValue(json, OffsetDateTime.class);

		// 원래 객체와 역직렬화된 객체가 같은지 확인
		assertThat(deserialized, equalTo(now));
	}

	@Test
	public void testOffsetDateTimeDeserialization() throws Exception {
		// 특정 형식의 OffsetDateTime 문자열
		String dateTimeString = "2024-11-29T00:00:00+09:00";

		// 문자열을 OffsetDateTime으로 역직렬화
		OffsetDateTime deserialized = objectMapper.readValue(
				"\"" + dateTimeString + "\"", OffsetDateTime.class);

		// 예상되는 OffsetDateTime 객체 생성
		OffsetDateTime expected = OffsetDateTime.parse(dateTimeString, DateTimeFormatter.ISO_OFFSET_DATE_TIME);

		// 역직렬화된 객체가 예상 객체와 같은지 확인
		assertThat(deserialized, equalTo(expected));
	}

}
