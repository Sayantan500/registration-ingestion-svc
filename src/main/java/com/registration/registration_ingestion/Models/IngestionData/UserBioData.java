package com.registration.registration_ingestion.Models.IngestionData;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class UserBioData {
    @JsonProperty("name")
    private final String Name;

    @JsonProperty("roll_no")
    private final int Roll_Number;

    @JsonProperty("registration_no")
    private final long Registration_Number;

    @JsonProperty("email")
    private final String Email;

    public String getEmail() {
        return Email;
    }
}
