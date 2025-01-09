package com.demoApp.demo.service;

import com.demoApp.demo.constants.CarConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    Logger logger = LoggerFactory.getLogger(KafkaService.class);

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    public Boolean updateLocation(String location){
        kafkaTemplate.send(CarConstants.Loaction_Topic,location);
        logger.info("message produced");
        return true;
    }
}
