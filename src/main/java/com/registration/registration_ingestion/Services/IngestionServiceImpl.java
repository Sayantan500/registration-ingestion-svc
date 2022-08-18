package com.registration.registration_ingestion.Services;

import com.registration.registration_ingestion.Models.RabbitMessage;
import com.registration.registration_ingestion.RabbitMQ.RabbitMessageProducer;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

@Service
final class IngestionServiceImpl implements IngestionService
{
    @Autowired
    private RabbitMessageProducer rabbitMessageProducer;

    @Value("${values.email_name_pattern_regex}")
    private String email_nameBox_regex;

    @Override
    public boolean enqueNewUserCreationRequest(RabbitMessage newUserToVerify) {
        return rabbitMessageProducer.EnqueueUserForVerification(newUserToVerify);
    }

    @Override
    public String getSessionID() {
        return UUID.randomUUID().toString();
    }

    public boolean verifyEmailDomainAndPattern(String email) {
        /**
         * sets a default value for the pattern of email name
         * if the value is not provided through the properties file.
         * */
        if(email_nameBox_regex == null || email_nameBox_regex.compareToIgnoreCase("")==0)
            email_nameBox_regex = "[[a-z]+[A-z]]*";
        final Pattern emailPattern = Pattern.compile(email_nameBox_regex);

        AtomicBoolean isMailboxNameOk = new AtomicBoolean(false);
        AtomicBoolean isDomainNameOk = new AtomicBoolean(false);

        final Thread mailBoxNameChecking = new Thread(
                () -> {
                    if(email.compareTo("")==0)
                        isMailboxNameOk.set(false);
                    final String mailBoxName = email.split("@")[0];
                    if (emailPattern.matcher(mailBoxName).matches())
                        isMailboxNameOk.set(true);
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
