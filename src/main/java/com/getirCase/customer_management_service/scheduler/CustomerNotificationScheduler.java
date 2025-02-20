package com.getirCase.customer_management_service.scheduler;

import com.getirCase.customer_management_service.entity.Customer;
import com.getirCase.customer_management_service.repository.CustomerRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Component
public class CustomerNotificationScheduler {

    private static final Logger logger = LoggerFactory.getLogger(CustomerNotificationScheduler.class);

    private final CustomerRepository customerRepository;

    public CustomerNotificationScheduler(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Scheduled(cron = "0 */5 * * * ?")
    public void checkCustomerLevels() {
        logger.info("Checking customer levels for notifications...");

        List<Customer> customers = customerRepository.findAll();

        for (Customer customer : customers) {
            int orderCount = customer.getOrderCount();

            if (orderCount == 9 || orderCount == 19) {
                sendNotification(customer.getId(), orderCount);
            }
        }
    }

    private void sendNotification(Long customerId, int orderCount) {
        String message = "You have placed " + orderCount + " orders with us. "
                + "Buy one more and you will be promoted!";

        logger.info("Notification sent to customer {}: {}", customerId, message);
        System.out.println("Notification: " + message);
    }
}
