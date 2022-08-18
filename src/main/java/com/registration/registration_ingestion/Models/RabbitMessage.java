package com.registration.registration_ingestion.Models;

import com.registration.registration_ingestion.Models.IngestionData.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class RabbitMessage {
    private final String Session_ID;
    private final User userToVerify;
}
