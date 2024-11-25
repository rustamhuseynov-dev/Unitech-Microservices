package com.rustam.ms_account.dto.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountIncreaseRequest {

    private BigDecimal balance;

    private String iban;
}
