package com.getirCase.customer_management_service.enums;

public enum KafkaTopics {
  CUSTOMER_EVENTS("customer.events");

  private final String topicName;

  KafkaTopics(String topicName) {
    this.topicName = topicName;
  }

  public String getTopicName() {
    return topicName;
  }
}
