package com.getirCase.customer_management_service.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.getirCase.customer_management_service.enums.KafkaEventType;
import com.getirCase.customer_management_service.model.event.CustomerEvent;
import com.getirCase.customer_management_service.service.handler.CustomerEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomerEventListener {

  private final ObjectMapper objectMapper;
  private final CustomerEventHandler eventHandler;

  public CustomerEventListener(ObjectMapper objectMapper, CustomerEventHandler eventHandler) {
    this.objectMapper = objectMapper.copy(); // Defensive copy
    this.eventHandler = eventHandler;
  }

  @KafkaListener(topics = "customer.events", groupId = "customer-service-group")
  public void consumeCustomerEvents(String message) {
    try {
      JsonNode jsonNode = objectMapper.readTree(message);
      String eventTypeStr = jsonNode.get("eventType").asText();
      KafkaEventType eventType = KafkaEventType.valueOf(eventTypeStr);

      CustomerEvent event = objectMapper.treeToValue(jsonNode, CustomerEvent.class);

      log.info("Received Kafka event: {}", event);

      switch (eventType) {
        case CUSTOMER_ORDER_CREATED_EVENT:
          eventHandler.handleCustomerOrderCreated(event);
          break;
        case CUSTOMER_TIER_UPDATED_EVENT:
          eventHandler.handleCustomerTierUpdated(event);
          break;
        default:
          log.warn("Unknown event type received: {}", eventType);
      }

    } catch (IllegalArgumentException e) {
      log.error("Invalid eventType in Kafka message: {}. Message: {}", e.getMessage(), message);
    } catch (Exception e) {
      log.error("Error processing Kafka event. Message: {}", message, e);
    }
  }
}
