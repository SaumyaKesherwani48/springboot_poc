package com.example.consumer.consumer;

import com.example.consumer.constants.ConsumerConstants;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class KafkaConsumer {

    @KafkaListener(topics = ConsumerConstants.Location_update_Topic,groupId = ConsumerConstants.Group_Id)
    public void updatedLocation(String msg){
        System.out.println(msg);
    }
}
