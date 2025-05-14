# RestCallsMQ
Testing REST calls with Message Queues

# Config
## Apache Kafka
You could use a local instance of Apache Kafka. The easiest way is to run a Docker instance.
- Create a file called docker-compose.yml with the next content:
``` yml
version: '3'
services:
  kafka:
    image: 'bitnami/kafka:latest'
    container_name: 'kafka'
    ports:
      - '9092:9092'
    environment:
      # KRaft settings
      - KAFKA_CFG_NODE_ID=0
      - KAFKA_CFG_PROCESS_ROLES=controller,broker
      - KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=0@kafka:9094
      - KAFKA_CFG_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9094
      - KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092
      - KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT
      - KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER
      - KAFKA_CFG_INTER_BROKER_LISTENER_NAME=PLAINTEXT
      - KAFKA_BROKER_ID=0 # Should match KAFKA_CFG_NODE_ID when KAFKA_CFG_PROCESS_ROLES includes "broker"
      - ALLOW_PLAINTEXT_LISTENER=yes
      - KAFKA_CFG_OFFSETS_TOPIC_REPLICATION_FACTOR=1
      - KAFKA_CFG_TRANSACTION_STATE_LOG_REPLICATION_FACTOR=1
      - KAFKA_CFG_TRANSACTION_STATE_LOG_MIN_ISR=1
```
- Then run the next command:
`docker-compose up -d`
- To create the topic run these commands:
`docker exec -it kafka bash`

`kafka-topics.sh --bootstrap-server localhost:9092 --create --topic event.update`

## Run the App
The app was developed with the Spring Boot framework and gradle, so you need to have Java installed, not be using
the 8080 port and run the command: `./gradlew bootRun`.

## Test the App
There are 3 events hardcoded in the app with IDs 1, 2 and 3. You can make request in the terminal with the next command
(you need to have the curl package installed):
``` bash
curl --location 'localhost:8080/events/status' \
--header 'Content-Type: application/json' \
--data '{
    "eventId": 1,
    "status": true
}'
```
And then you can change the values ==eventId== and ==status==. You can also import it in Postman, which is my recommendation.

## Run unit test
The unit tests are located in the ==/src/test== folder. The basic unit test, that test the main logic, is EventServiceImplTest.
I suggest to run this test with an IDE because is the easiest way to do it.

# Project structure
The project was created with a layer architecture with the next package structure: controller, service, model, repository. This structure
was chose because is simple and organized for small projects like this one.

# AI usage
The usage of AI was limited mainly because I don't have any AI tool attached to my IDE (Idea CE). So I had to copy the code to Gemini,
which I chose because the last one iteration is good with code generation, and then copy back the result into the project and review it.
This mode is slower than use copilot or other IDE integrations. The main use of AI was for two specific point, one was to choose the right  
class/tool for implement the concurrency. The other use of AI was made for complete the unit test. In the first case I use the code generated
by the AI and adapted to the use of my application, cleaning it and separating the responsibilities. In the second case I changed some pieces
of code to be more accurate with the class I was testing and I deleted other code that wasn't required.
I also use the AI before start to code to learn how to run and configure kafka in my PC.