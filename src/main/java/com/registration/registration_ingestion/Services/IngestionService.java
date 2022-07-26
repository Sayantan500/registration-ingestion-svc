package com.registration.registration_ingestion.Services;

import com.registration.registration_ingestion.Models.IngestionData.User;

public interface IngestionService {
    boolean verifyEmailDomainAndPattern(String email);
    boolean enqueNewUserCreationRequest(User newUserToVerify);
}
