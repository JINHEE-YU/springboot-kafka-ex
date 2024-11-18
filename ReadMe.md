# 프로젝트 설명
Springboot를 이용하여 Kafka와 연동여 기본적인 통신이 가능한 프로젝트 구축
* kafka를 docker-compose를 이용하여 구축할 수 있도록 compose파일(`kafka-compose.yml`)을 추가 함
* `KafkaExAPIController.java`파일에 API를 이용하여 테스트 할 수 있도록 구축 함 
* 또한, `SpringbootKafkaExApplicationTests.java`파일에 EmbeddedKafka를 이용하여 테스트 할 수 있도록 구축 함

## kafka 실행 방법
* kafka 실행 : `docker-compose -f kafka-compose.yml up`
* container 진입 : `docker exec -it kafka bash`

### Topic을 이용한 실행방법
* consumer 실행 : `/opt/kafka_2.13-2.8.1/bin/kafka-console-consumer.sh --topic ${TOPIC} --bootstrap-server kafka:9092`
* producer 실행 : `/opt/kafka_2.13-2.8.1/bin/kafka-console-producer.sh --topic ${TOPIC} --broker-list kafka:9092`

### API를 이용한 실행방법
* curl 테스트 : `curl http://localhost:8090/${URI}`

### URI, Topic 목록
* 송수신 데이터의 단/복수개에 따른 구현 형태의 차이를 보기 위해 구분하여 구현

|Method|URI|Explanation|TOPIC|
|------|---|-------|--------|
|GET|/send|고정된 String타입의 message값 전송<br>복수개를 전송하는 경우 `,`로 구분하여 전송|topic1|
|GET|/send/msg|String타입의 message값을 List형태로 전송<br>`추출`로 시작하는 데이터 필터기능 추가|mgs-filter|
|GET|/send2|고정된 UserDTO타입의 단일개의 값 전송|second|
|POST|/send2/user|UserDTO타입의 단일개의 값 전송<br> `age가 30 이하`인 데이터만 수신|topic2|
|POST|/send2/users|UserDTO타입의 1개이상의 값 전송(JSON형식의 List형태)|dto-list|
