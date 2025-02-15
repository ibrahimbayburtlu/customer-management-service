package com.getirCase.customer_management_service.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.getirCase.customer_management_service.model.CustomerEvent;
import com.getirCase.customer_management_service.service.handler.CustomerEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomerEventConsumer {

    private final ObjectMapper objectMapper;
    private final CustomerEventHandler eventHandler;

    public CustomerEventConsumer(ObjectMapper objectMapper, CustomerEventHandler eventHandler) {
        this.objectMapper = objectMapper;
        this.eventHandler = eventHandler;
    }

    @KafkaListener(topics = "customer.events", groupId = "customer-service-group")
    public void consumeCustomerEvents(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            String eventType = jsonNode.get("eventType").asText();

            CustomerEvent event = objectMapper.treeToValue(jsonNode, CustomerEvent.class);

            log.info("Received event: {}", event);

            switch (eventType) {
                case "CUSTOMER_CREATED":
                    eventHandler.handleCustomerCreated(event);
                    break;
                case "CUSTOMER_UPDATED":
                    eventHandler.handleCustomerUpdated(event);
                    break;
                case "CUSTOMER_ORDER_PLACED":
                    eventHandler.handleCustomerOrderPlaced(event);
                    break;
                case "CUSTOMER_TIER_UPDATED_EVENT":
                    eventHandler.handleCustomerTierUpdated(event);
                    break;
                default:
                    log.warn("Unknown event type received: {}", eventType);
            }

        } catch (Exception e) {
            log.error("Error processing Kafka event: ", e);
        }
    }
}
