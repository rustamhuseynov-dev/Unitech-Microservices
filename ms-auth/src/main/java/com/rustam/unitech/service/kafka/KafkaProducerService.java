package com.rustam.unitech.service.kafka;

import com.rustam.unitech.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(UserResponse response) {
        kafkaTemplate.send("events-notification", response);
    }

}
