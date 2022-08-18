package com.registration.registration_ingestion.RabbitMQ;

import com.registration.registration_ingestion.Models.RabbitMessage;

@FunctionalInterface
public interface RabbitMessageProducer {
    /*boolean EnqueueUserForVerification(UserToVerify userToVerification);*/
    boolean EnqueueUserForVerification(RabbitMessage userToVerification);
}
