package com.registration.registration_ingestion.RabbitMQ;

public interface RabbitConfigConstants
{
    String QUEUE_NAME = "WAITING_FOR_VERIFICATION";
    String EXCHANGE_NAME = "REGISTRATION_EXCHANGE";
    String ROUTING_KEY = "VERIFICATION_WAITLIST_ROUTING_KEY";
}
