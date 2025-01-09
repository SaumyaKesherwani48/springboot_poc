package com.Demo.demoApplication.config;

import com.Demo.demoApplication.constants.CarConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;


@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic topic(){
        return TopicBuilder.name(CarConstants.Loaction_Topic).build();
    }
}
