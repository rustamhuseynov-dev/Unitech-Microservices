package com.rustam.unitech.service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "events-auth")
    public void receiveMessage(String message) {
        System.out.println("Received message: " + message);
    }
}
