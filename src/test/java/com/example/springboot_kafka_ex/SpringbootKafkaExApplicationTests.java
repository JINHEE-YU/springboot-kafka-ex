package com.example.springboot_kafka_ex;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import com.example.springboot_kafka_ex.kafka.consumer.KafkaConsumer;
import com.example.springboot_kafka_ex.kafka.producer.KafkaProducer;

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

}
