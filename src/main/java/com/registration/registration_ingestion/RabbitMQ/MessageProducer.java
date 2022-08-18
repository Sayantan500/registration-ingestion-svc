package com.registration.registration_ingestion.RabbitMQ;

import com.registration.registration_ingestion.Models.RabbitMessage;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class MessageProducer implements RabbitMessageProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    RabbitMQConfiguration rabbitMQConfiguration;

    @Override
    public boolean EnqueueUserForVerification(RabbitMessage userToVerify) {
        try{
            rabbitTemplate.convertAndSend(
                    rabbitMQConfiguration.getEXCHANGE_NAME(),
                    rabbitMQConfiguration.getROUTING_KEY(),
                    userToVerify
            );
        }catch (AmqpException amqpException){
            System.out.println("[ MessageProducer.EnqueueUserForVerification() ] " + amqpException.getMessage());
            return false;
        }
        return true;
    }
}
