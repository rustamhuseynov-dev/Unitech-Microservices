package com.rustam.ms_account.util;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IbanService {

    public String generateIbanForUser() {
        String countryCode = "AZ";
        String checkDigits = "00";
        String uniqueAccountPart = UUID.randomUUID().toString().replace("-", "").substring(0, 22);

        return countryCode + checkDigits + uniqueAccountPart;
    }
}
