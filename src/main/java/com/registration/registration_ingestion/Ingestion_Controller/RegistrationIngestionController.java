package com.registration.registration_ingestion.Ingestion_Controller;

import com.registration.registration_ingestion.Models.IngestionData.User;
import com.registration.registration_ingestion.Models.RabbitMessage;
import com.registration.registration_ingestion.Services.IngestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register/new")
public class RegistrationIngestionController {

    @Autowired
    private IngestionService ingestionService;

    @PostMapping
    public ResponseEntity<String> registerNewUser(@RequestBody User newUser){
        if(!ingestionService.isUserAlreadyPresent(newUser.getEmail(), newUser.getPassword()))
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("User Already Present...");

        if(ingestionService.verifyEmailDomainAndPattern(newUser.getEmail()))
        {
            final String sessionID = ingestionService.getSessionID();
            final RabbitMessage rabbitMessage = new RabbitMessage(sessionID,newUser);

            if(ingestionService.enqueNewUserCreationRequest(rabbitMessage))
            {
                HttpHeaders headers = new HttpHeaders();
                headers.add(
                        "set-cookie",
                        ("session_id = " + sessionID) +
                                ";httponly"
                        );
                return ResponseEntity
                        .status(HttpStatus.ACCEPTED)
                        .headers(headers)
                        .body("Check your email for verification code...");
            }
            else
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Please try after sometime...");
        }
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body("Email Address provided is Invalid...");
    }
}