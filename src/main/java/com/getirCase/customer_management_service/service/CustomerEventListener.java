package com.getirCase.customer_management_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.getirCase.customer_management_service.entity.Customer;
import com.getirCase.customer_management_service.enums.CustomerTier;
import com.getirCase.customer_management_service.exception.CustomerNotFoundException;
import com.getirCase.customer_management_service.repository.CustomerRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CustomerEventListener {
    private static final Logger logger = LoggerFactory.getLogger(CustomerEventListener.class);

    private final CustomerRepository customerRepository;
    private final ObjectMapper objectMapper;

    public CustomerEventListener(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.objectMapper = new ObjectMapper();
    }

    @KafkaListener(
            topics = "#{@environment.getProperty('spring.kafka.topics.customer-tier-update')}",
            groupId = "#{@environment.getProperty('spring.kafka.consumer.group-id')}")
    @Retryable(
            value = { Exception.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000))
    public void handleCustomerTierUpdate(ConsumerRecord<String, String> record) {
        try {
            logger.info("Received Kafka Message: {}", record.value());

            JsonNode jsonNode = objectMapper.readTree(record.value());
            Long customerId = jsonNode.get("customerId").asLong();
            int orderCount = jsonNode.get("orderCount").asInt();

            logger.info("Processing customer tier update for customerId: {} with orderCount: {}", customerId, orderCount);


            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerId));


            customer.setOrderCount(orderCount);

            CustomerTier newTier;
            if (orderCount >= 20) {
                newTier = CustomerTier.PLATINUM;
            } else if (orderCount >= 10) {
                newTier = CustomerTier.GOLD;
            } else {
                newTier = CustomerTier.REGULAR;
            }

            if (!customer.getTier().equals(newTier)) {
                customer.setTier(newTier);
                customerRepository.save(customer);
                logger.info("Customer ID: {} updated to tier: {}", customerId, newTier);
            } else {
                logger.info("Customer ID: {} remains in tier: {}", customerId, newTier);
            }

        } catch (CustomerNotFoundException e) {
            logger.warn("Customer not found: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Error processing Kafka message: {}", e.getMessage(), e);
            throw new RuntimeException("Error processing Kafka event", e);
        }
    }
}
