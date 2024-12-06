package com.rustam.ms_notification_service.service;

import com.rustam.ms_notification_service.dto.SendMailDto;
import com.rustam.ms_notification_service.dto.VerificationSendDto;
import com.rustam.ms_notification_service.util.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendMailService {

    private final EmailSender emailSender;

    @KafkaListener(topics = "events-notification",containerFactory = "customKafkaListenerContainerFactory")
    public void listenerSendMail(String username){
        log.info("events-notification {}",username);
        sendEmailToUser(username);
    }

    private void sendEmailToUser(String username) {
        emailSender.sendEmail("rustam.huseynov.2024@gmail.com", "User Update", "User information has been updated: " + username);
    }

    @KafkaListener(topics = "events-notification-verification",containerFactory = "verificationKafkaListenerContainerFactory")
    public void listenerSendEmailVerification(VerificationSendDto verificationSendDto){
        log.info("events-notification-notification {}",verificationSendDto.getEmail());
        log.info("events-notification-notification {}",verificationSendDto.getName());
        sendEmailVerification(verificationSendDto);
    }

    private void sendEmailVerification(VerificationSendDto verificationSendDto) {
        emailSender.sendEmail(verificationSendDto.getEmail(), "Hi Mr." + verificationSendDto.getName() , "Visit this link and update your password: http://localhost:8085/api/v1/auth/reset-password");
    }
}
