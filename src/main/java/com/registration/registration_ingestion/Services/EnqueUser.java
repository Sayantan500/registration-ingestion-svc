package com.registration.registration_ingestion.Services;

import com.registration.registration_ingestion.Models.IngestionData.User;
import com.registration.registration_ingestion.RabbitMQ.RabbitMessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "enque_user_svc")
class EnqueUser implements IngestionService {

    @Autowired
    private RabbitMessageProducer rabbitMessageProducer;
    @Override
    public boolean enqueNewUserCreationRequest(User newUserToVerify) {
        return rabbitMessageProducer.EnqueueUserForVerification(newUserToVerify);
    }
}
