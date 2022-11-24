package com.registration.registration_ingestion.Models.IngestionData;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
class UserCredentials{
    @JsonProperty("email")
    private final String Email;

    @JsonProperty("passphrase")
    private final String Password;
}
