package com.rustam.ms_notification_service.service;

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

    @KafkaListener(topics = "events-notification")
    public void listenerSendMail(Object sendMail){
        log.info("events-notification {}",sendMail);
    }

}
