package com.rustam.ms_account.dto.request;

import com.rustam.ms_account.dao.enums.AccountStatus;
import com.rustam.ms_account.dao.enums.Currency;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountRequest {

    @NotBlank
    private String username;

    private Currency currency;
}
