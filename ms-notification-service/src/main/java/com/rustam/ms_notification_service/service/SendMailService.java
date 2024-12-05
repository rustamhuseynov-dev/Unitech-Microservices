package com.rustam.ms_notification_service.service;

import com.rustam.ms_notification_service.dto.SendMailDto;
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

}
