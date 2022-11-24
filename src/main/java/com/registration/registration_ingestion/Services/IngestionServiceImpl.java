package com.registration.registration_ingestion.Services;

import com.registration.registration_ingestion.Models.LoginRequestBody;
import com.registration.registration_ingestion.Models.RabbitMessage;
import com.registration.registration_ingestion.RabbitMQ.RabbitMessageProducer;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

@Service
final class IngestionServiceImpl implements IngestionService
{
    private final RabbitMessageProducer rabbitMessageProducer;
    private final RestTemplate restTemplate;
    private final String baseUrlLogin;
    private final String email_nameBox_regex;

    @Autowired
    public IngestionServiceImpl(
            RabbitMessageProducer rabbitMessageProducer,
            RestTemplate restTemplate,
            @Qualifier("login_base_url") String baseUrlLogin,
            @Qualifier("email_name_pattern") String email_nameBox_regex
    )
    {
        this.rabbitMessageProducer = rabbitMessageProducer;
        this.restTemplate = restTemplate;
        this.baseUrlLogin = baseUrlLogin;

        if(email_nameBox_regex == null || email_nameBox_regex.compareToIgnoreCase("")==0)
            this.email_nameBox_regex = "[[a-z]+[A-z]]*";
        else
            this.email_nameBox_regex = email_nameBox_regex;
    }

    @Override
    public boolean enqueNewUserCreationRequest(RabbitMessage newUserToVerify) {
        return rabbitMessageProducer.EnqueueUserForVerification(newUserToVerify);
    }

    @Override
    public String getSessionID() {
        return UUID.randomUUID().toString();
    }

    @Override
    public boolean isUserAlreadyPresent(String email, String password) {
        String requestUrl = baseUrlLogin + "/signin";
        try
        {
            restTemplate.exchange(
                    requestUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(new LoginRequestBody(email,password)),
                    Object.class,
                    1
            );
        }
        catch (RuntimeException exception)
        {
            return true; //means any 4xx error code is received implying user is not present.
        }
        return false;
    }

    public boolean verifyEmailDomainAndPattern(String email) {
        /*
          sets a default value for the pattern of email name
          if the value is not provided through the properties file.
          */
        /*if(email_nameBox_regex == null || email_nameBox_regex.compareToIgnoreCase("")==0)
            email_nameBox_regex = "[[a-z]+[A-z]]*";*/
        final Pattern emailPattern = Pattern.compile(email_nameBox_regex);

        AtomicBoolean isMailboxNameOk = new AtomicBoolean(false);
        AtomicBoolean isDomainNameOk = new AtomicBoolean(false);

        final Thread mailBoxNameChecking = new Thread(
                () -> {
                    if(email.compareTo("")==0)
                        isMailboxNameOk.set(false);
                    final String mailBoxName = email.split("@")[0];
                    isMailboxNameOk.set(
                            mailBoxName.length() >= 7 && emailPattern.matcher(mailBoxName).matches()
                    );
                }
        );

        final Thread domainNameChecking = new Thread(
                () -> {
                    if(email.compareTo("")==0)
                        isDomainNameOk.set(false);
                    final String domainName = email.split("@")[1];
                    if (domainName.compareTo("kiit.ac.in") == 0)
                        isDomainNameOk.set(true);
                }
        );

        mailBoxNameChecking.start();
        domainNameChecking.start();

        try{
            mailBoxNameChecking.join();
            domainNameChecking.join();
        }catch (InterruptedException interruptedException){
            LoggerFactory.getILoggerFactory().getLogger("Verification of Email").error(interruptedException.getMessage());
            return false;
        }

        return (isMailboxNameOk.get() && isDomainNameOk.get());
    }
}
