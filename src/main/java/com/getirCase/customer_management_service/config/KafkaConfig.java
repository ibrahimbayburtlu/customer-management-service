package com.getirCase.customer_management_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.topics.customer-tier-update}")
    private String customerTierUpdateTopic;

    @Value("${spring.kafka.consumer.group-id}")
    private String kafkaGroupId;

    @Bean
    public String getCustomerTierUpdateTopic() {
        return customerTierUpdateTopic;
    }

    @Bean
    public String getKafkaGroupId() {
        return kafkaGroupId;
    }
}
