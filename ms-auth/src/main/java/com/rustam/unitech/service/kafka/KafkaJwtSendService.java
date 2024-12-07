package com.rustam.unitech.service.kafka;

import com.rustam.unitech.dto.kafka.VerificationSendDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaJwtSendService {

    private final KafkaTemplate<String,Object> kafkaTemplate;

    public void sendJwt(String jwt) {
        kafkaTemplate.send("events-account-jwt", jwt);
    }
}
