package com.rustam.ms_account.service.kafka;

import com.rustam.ms_account.dto.request.AccountRequest;
import com.rustam.ms_account.service.AccountService;
import com.rustam.ms_account.util.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {

    private final JwtUtil jwtUtil;
    private final AccountService accountService;

    @KafkaListener(topics = "events-account-jwt", containerFactory = "customKafkaListenerContainerFactory")
    public void jwtListener(String jwt) {
        log.info("userId {}",jwt);
        String id = jwtUtil.extractSubject(jwt);
        AccountRequest accountRequest = new AccountRequest();
        accountService.createAccount(accountRequest);
    }
}
