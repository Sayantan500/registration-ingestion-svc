package com.registration.registration_ingestion.RabbitMQ;

import com.registration.registration_ingestion.Models.IngestionData.User;

@FunctionalInterface
public interface RabbitMessageProducer {
    boolean EnqueueUserForVerification(User userToVerification);
}
