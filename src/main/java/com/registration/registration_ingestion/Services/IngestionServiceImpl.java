package com.registration.registration_ingestion.Services;

import com.registration.registration_ingestion.Models.IngestionData.User;
import com.registration.registration_ingestion.RabbitMQ.RabbitMessageProducer;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

@Service
final class IngestionServiceImpl implements IngestionService
{
    @Autowired
    private RabbitMessageProducer rabbitMessageProducer;

    @Override
    public boolean enqueNewUserCreationRequest(User newUserToVerify) {
        return rabbitMessageProducer.EnqueueUserForVerification(newUserToVerify);
    }

    public boolean verifyEmailDomainAndPattern(String email) {
        Pattern emailPattern = Pattern.compile("\\d*");
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
