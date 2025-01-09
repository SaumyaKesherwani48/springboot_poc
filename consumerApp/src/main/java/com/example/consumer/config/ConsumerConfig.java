package com.example.consumer.config;

import com.example.consumer.constants.ConsumerConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
public class ConsumerConfig {

    @KafkaListener(topics = ConsumerConstants.Location_update_Topic,groupId = ConsumerConstants.Group_Id)
    public void updatedLocation(String location){

        System.out.println(location);

    }

}
