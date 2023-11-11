package com.aruna.config;

import com.aruna.constant.ConfigConstant;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public NewTopic createTopic(){
        return new NewTopic(ConfigConstant.kafkaTopic, 3, (short) 1);
    }
}
