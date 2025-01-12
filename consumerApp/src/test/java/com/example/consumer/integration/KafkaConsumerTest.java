package com.example.consumer.integration;

import com.example.consumer.constants.ConsumerConstants;
import com.example.consumer.consumer.KafkaConsumer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@EmbeddedKafka(partitions = 1,topics = {ConsumerConstants.model_update_Topic})
public class KafkaConsumerTest {

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    private KafkaConsumer kafkaConsumer;

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String servers;

    @Test
    public void testKafkaConsumerMsg() throws InterruptedException{

        String msg = "CarModel created successfully!!";

        kafkaTemplate.send(ConsumerConstants.model_update_Topic,msg);

        Thread.sleep(2000);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        kafkaConsumer.updateCarModel(msg);

        assertTrue(outputStream.toString().contains(msg));
    }


}
