package com.registration.registration_ingestion.Models.IngestionData;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

@Component
class userBioData {
    @JsonProperty("name")
    private String Name;

    @JsonProperty("roll_no")
    private int Roll_Number;

    @JsonProperty("registration_no")
    private long Registration_Number;

    @JsonProperty("email")
    private String Email;

    public String getEmail() {
        return Email;
    }
}
