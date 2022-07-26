package com.registration.registration_ingestion;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@SpringBootTest
class RegistrationIngestionApplicationTests {
    @Value("${my.annotated.value}")
    String dataFromValueAnnotation;

    @Test
    public void testingSpringValueAnnotation(){
        if(dataFromValueAnnotation.compareTo("Value from annotation")==0)
            System.out.println("Value Received using Spring's @Value annotation : " + dataFromValueAnnotation);
        else
            System.out.println("Test Failed : " + dataFromValueAnnotation);
    }

    @ParameterizedTest
    @CsvFileSource(files = "src/test/Email_domain_and_pattern_verification_testcases.csv")
    public void verifyEmailDomainAndPattern(String email) throws InterruptedException {
        //boolean result = true;
        Pattern emailPattern = Pattern.compile("\\d*");
        AtomicBoolean isMailboxNameOk = new AtomicBoolean(false);
        AtomicBoolean isDomainNameOk = new AtomicBoolean(false);

        Thread mailBoxNameChecking = new Thread(
                () -> {
                    String mailBoxName = email.split("@")[0];
                    try {
                        assert (emailPattern.matcher(mailBoxName).matches());
                        isMailboxNameOk.set(true);
                    } catch (AssertionError assertionError) {
                        System.out.println("Email Pattern invalid...");
                        return;
                    }
                    System.out.println("mailBoxName = " + mailBoxName);
                }
        );

        Thread domainNameChecking = new Thread(
                () -> {
                    String domainName = email.split("@")[1];
                    try {
                        assert (domainName.compareTo("kiit.ac.in") == 0);
                        isDomainNameOk.set(true);
                    } catch (AssertionError assertionError) {
                        System.out.println("Domain name invalid...");
                        return;
                    }
                    System.out.println("domainName = " + domainName);
                }
        );
        mailBoxNameChecking.start();
        domainNameChecking.start();

        mailBoxNameChecking.join();
        domainNameChecking.join();
        System.out.println("\nAll threads joined...");
        try {
            assert (isMailboxNameOk.get() && isDomainNameOk.get());
            Logger.getAnonymousLogger().info("\nValid Email...\n");
        } catch (AssertionError assertionError) {
            Logger.getAnonymousLogger().info("\nInvalid Email...\n");
            //result = false;
        }
        //return result;
    }

}
