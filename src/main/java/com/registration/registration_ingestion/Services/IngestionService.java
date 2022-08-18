package com.registration.registration_ingestion.Services;

import com.registration.registration_ingestion.Models.RabbitMessage;

public interface IngestionService {
    boolean verifyEmailDomainAndPattern(String email);
    boolean enqueNewUserCreationRequest(RabbitMessage newUserToVerify);
    String getSessionID();
}
