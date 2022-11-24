package com.registration.registration_ingestion.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class LoginRequestBody
{
    @JsonProperty("username")
    private String email;

    @JsonProperty("password")
    private String password;
}
