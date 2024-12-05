package com.rustam.unitech.service.kafka;

import com.rustam.unitech.dto.kafka.VerificationSendDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaVerificationService {

    private final KafkaTemplate<String,Object> kafkaTemplate;

    public void sendMessageVerification(VerificationSendDto verificationSendDto) {
        kafkaTemplate.send("events-notification-verification", verificationSendDto);
    }
}
