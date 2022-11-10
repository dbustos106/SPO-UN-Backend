package com.app.spoun.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class EmailSenderService{

    private JavaMailSender mailSender;
    private static Logger LOGGER;

    @Autowired
    public EmailSenderService(JavaMailSender mailSender){
        this.mailSender = mailSender;
        LOGGER = LoggerFactory.getLogger(EmailSenderService.class);
    }


    @Async
    public void send(String toEmail, String subject, String content) throws MessagingException, UnsupportedEncodingException {
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom("spoun.app@gmail.com", "spo-un");
            helper.setSubject(subject);
            helper.setTo(toEmail);

            helper.setText(content, true);
            mailSender.send(mimeMessage);
        }catch (MessagingException e){
            LOGGER.error("failed to send email", e);
            throw new IllegalStateException("Failed to send email");
        }
    }

}
