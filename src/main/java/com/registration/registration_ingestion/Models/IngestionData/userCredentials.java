package com.registration.registration_ingestion.Models.IngestionData;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

@Component
class userCredentials {
    @JsonProperty("email")
    private String Email;

    @JsonProperty("passphrase")
    private String Password;
}
