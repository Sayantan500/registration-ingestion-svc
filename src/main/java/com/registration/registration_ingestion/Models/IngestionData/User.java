package com.registration.registration_ingestion.Models.IngestionData;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    @JsonProperty("biodata")
    private userBioData User_BioData;

    @JsonProperty("credentials")
    private userCredentials User_Credentials;

    public String getEmail(){
        return User_BioData.getEmail();
    }
}
