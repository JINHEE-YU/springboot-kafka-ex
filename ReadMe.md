# 프로젝트 설명
Springboot를 이용하여 Kafka와 연동여 기본적인 통신이 가능한 프로젝트 구축
* kafka를 docker-compose를 이용하여 구축할 수 있도록 compose파일(`kafka-compose.yml`)을 추가 함
* `KafkaExAPIController.java`파일에 API를 이용하여 테스트 할 수 있도록 구축 함 
* 또한, `SpringbootKafkaExApplicationTests.java`파일에 EmbeddedKafka를 이용하여 테스트 할 수 있도록 구축 함

## kafka 실행 방법
  * kafka 실행 : `docker-compose -f kafka-compose.yml up`
    
  * container 진입 : `docker exec -it kafka bash`
  * consumer 실행 : `/opt/kafka_2.13-2.8.1/bin/kafka-console-consumer.sh --topic topic1 --bootstrap-server kafka:9092`
  * producer 실행 : `/opt/kafka_2.13-2.8.1/bin/kafka-console-producer.sh --topic topic1 --broker-list kafka:9092`

  * curl 테스트 : `curl http://localhost:8090/send`
