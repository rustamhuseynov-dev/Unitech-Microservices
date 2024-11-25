package com.rustam.ms_account.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountIncreaseResponse {

    private BigDecimal balance;

    private String iban;

    private Long increaseBalanceAt;
}
