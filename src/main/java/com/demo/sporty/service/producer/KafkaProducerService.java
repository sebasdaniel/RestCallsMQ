package com.demo.sporty.service.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class KafkaProducerService  implements ProducerService {

    private static final String SERVER_CONFIG = "localhost:9092";
    private static final String TOPIC_NAME = "event.update";

    private KafkaProducer<String, String> producer;

    public KafkaProducerService() {
        init();
    }

    @Override
    public void send(String key, String message) {
        // Create a ProducerRecord
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, key, message);

        // Send the record asynchronously
        producer.send(record, (metadata, exception) -> {
            if (exception == null) {
                System.out.println("Message sent successfully. Offset: " + metadata.offset());
            } else {
                System.err.println("Error sending message: " + exception.getMessage());
            }
        });

        // Important: Ensure all pending sends are completed before closing
        producer.flush();
    }

    private void init() {
        // Configure producer properties
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, SERVER_CONFIG);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // Create a Kafka producer instance
        producer = new KafkaProducer<>(properties);
    }
}
