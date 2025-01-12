package com.demoApp.demo.integrationTest;

import com.demoApp.demo.service.KafkaService;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@EmbeddedKafka(brokerProperties = {"listeners=PLAINTEXT://localhost:9092"}, partitions = 1)
public class KafkaIntegrationTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private KafkaService kafkaService;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    private Consumer<String, String> consumer;

    @BeforeEach
    public void setUp() {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("location-topic", "true", embeddedKafkaBroker);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class);
        DefaultKafkaConsumerFactory<String,String> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProps);

        consumer = consumerFactory.createConsumer();
        consumer.subscribe(Collections.singletonList("location-topic"));
    }

    @AfterEach
    public void tearDown() {
        consumer.close();
    }

    @Test
    void testProducer() throws Exception {
        kafkaTemplate.send("location-topic", "test-key", "test-message");
        ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(consumer);
        assertThat(records.count()).isGreaterThan(0);

        records.forEach(record -> {
            assertThat(record.key()).isEqualTo("test-key");
            assertThat(record.value()).isEqualTo("test-message");
        });
    }
}
