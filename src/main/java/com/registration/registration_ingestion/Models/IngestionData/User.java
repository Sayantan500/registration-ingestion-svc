package com.registration.registration_ingestion.Models.IngestionData;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class User {
    @JsonProperty("biodata")
    private final UserBioData User_BioData;

    @JsonProperty("credentials")
    private final UserCredentials User_Credentials;

    public String getEmail() {
        return User_Credentials.getEmail();
    }

    public String getPassword(){
        return User_Credentials.getPassword();
    }
}
