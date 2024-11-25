package com.rustam.ms_account.dto.response;


import com.rustam.ms_account.dao.enums.AccountStatus;
import com.rustam.ms_account.dao.enums.Currency;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountResponse {

    private UUID id;
    private String iban;
    private String username;
    private Currency currency;
    private BigDecimal balance;
    private AccountStatus status;
    private Long createdAt;
    private Long updatedAt;
}
