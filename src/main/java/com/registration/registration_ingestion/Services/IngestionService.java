package com.registration.registration_ingestion.Services;

import com.registration.registration_ingestion.Models.IngestionData.User;

public interface IngestionService {
    default boolean verifyEmailDomainAndPattern(String email){
        return false;
    }
    default boolean enqueNewUserCreationRequest(User NewUser){
        return false;
    }
}
