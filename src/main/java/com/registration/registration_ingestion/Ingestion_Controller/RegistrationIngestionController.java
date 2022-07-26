package com.registration.registration_ingestion.Ingestion_Controller;

import com.registration.registration_ingestion.Models.IngestionData.User;
import com.registration.registration_ingestion.Services.IngestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/register")
public class RegistrationIngestionController {

    @Autowired
    @Qualifier(value = "verify_email_domain_and_pattern_svc")
    private IngestionService emailPatterAndDomainVerify;

    @Autowired
    @Qualifier(value = "enque_user_svc")
    private IngestionService userCreationRequestEnque;

    @PostMapping
    public ResponseEntity<String> registerNewUser(@RequestBody User newUser){

        //TODO :
        // Check if the user is already present in firebase auth database or not,
        // to prevent creation of multiple same user

        if(emailPatterAndDomainVerify.verifyEmailDomainAndPattern(newUser.getEmail()))
        {
            if (userCreationRequestEnque.enqueNewUserCreationRequest(newUser))
                return ResponseEntity
                        .status(HttpStatus.ACCEPTED)
                        .body("Check your email for verification code...");
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