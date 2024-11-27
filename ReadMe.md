# 프로젝트 설명
Springboot를 이용하여 Kafka와 연동여 기본적인 통신이 가능한 프로젝트 구축
## 주요 파일
* `kafka-compose.yml` : docker-compose를 이용하여 kafka를 구축할 수 있도록 세팅한 compose파일
* `postgre-compose.yml` : docker-compose를 이용하여 TimescaleDB를 구축할 수 있도록 세팅한 compose파일
* `KafkaExAPIController.java` : API를 이용하여 Kafka 테스트를 할 수 있도록 구축
* `SpringbootKafkaExApplicationTests.java` : EmbeddedKafka를 이용하여 테스트 할 수 있도록 구축
* `PostgreAPIController.java` : JPA형태로 TimescaleDB에 접근할 수 있는 기능 구축

# Docker
## kafka 실행 방법
###  Container 실행 방법
* kafka 실행 : `docker-compose -f kafka-compose.yml up`
* container 진입 : `docker exec -it kafka bash`
### Topic을 이용한 실행방법
* consumer 실행 : `/opt/kafka_2.13-2.8.1/bin/kafka-console-consumer.sh --topic ${TOPIC} --bootstrap-server kafka:9092`
* producer 실행 : `/opt/kafka_2.13-2.8.1/bin/kafka-console-producer.sh --topic ${TOPIC} --broker-list kafka:9092`
## TimescaleDB(Postgresql)
해당 컨테이너는 TimescaleDB로, PostgreSQL을 기반의 오픈소스 시계열(Time-series) RDBMS이다.
* TimescaleDB 실행 : `docker-compose -f postgre-compose.yml up`

# Test
## kafka 테스트
### API를 이용한 실행방법
* curl 테스트 : `curl http://localhost:8090/${URI}`
* 테스트 가능한 : Postman 파일 첨부
### URI, Topic 목록
* KAFKA테스트를 위한 API의 경우 송수신 데이터의 단/복수개에 따른 구현 형태의 차이를 보기 위해 구분하여 구현
* `/send`또는 `/test`로 시작하면 Kafka 용
* `/jpa`로 시작하면 TimescaleDB 용
  
| Method | URI             | Explanation                                                                         | TOPIC       |
| ------ | --------------- | ----------------------------------------------------------------------------------- | ----------- |
| GET    | /send           | 고정된 String타입의 message값 전송<br>복수개를 전송하는 경우 `,`로 구분하여 전송    | topic1      |
| GET    | /send2          | 고정된 UserDTO타입의 단일개의 값 전송                                               | second      |
| GET    | /send/msg       | String타입의 message값을 전송(`,`로 구분)<br>`추출`로 시작하는 데이터 필터기능 추가 | mgs-filter  |
| GET    | /send/msgs      | String타입의 message값을 List형태로 전송<br>`추출`로 시작하는 데이터 필터기능 추가  | mgs-filter  |
| GET    | /test/groupid   | GroupId테스트 - groupid에따라 소비자가 메시지를 병렬로 처리하는 현상 확인           | group-test  |
| GET    | /test/partition | Partition테스트 - message-key에 따라 파티션이 나눠 들어가는 현상 확인               | second      |
| POST   | /send/user      | UserDTO타입의 단일개의 값 전송<br> `age가 30 이하`인 데이터만 수신                  | topic2      |
| POST   | /send/users     | UserDTO타입의 1개 이상의 값을 List형태로 전송                                       | dto-list    |
| GET    | /jpa/person     | TimescaleDB의 person 전체 데이터 조회                                               | stock-price |
| POST   | /jpa/person     | TimescaleDB의 단일개 person 데이터 저장                                             | stock-price |
| GET    | /jpa/stock      | TimescaleDB의 stock_prices 전체 데이터 조회                                         | stock-price |
| POST   | /jpa/stock      | TimescaleDB의 1개 이상의 stock_prices 데이터 저장(JSON형식의 List형태)              | stock-price |
| POST   | /jpa/stock/send | Kafka를 통해 TimescaleDB 1개 이상의 stock_prices 데이터 저장(JSON형식의 List형태)   | stock-price |
