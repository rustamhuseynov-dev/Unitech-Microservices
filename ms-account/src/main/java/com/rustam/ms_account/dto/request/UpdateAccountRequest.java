package com.rustam.ms_account.dto.request;

import com.rustam.ms_account.dao.enums.AccountStatus;
import com.rustam.ms_account.dao.enums.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateAccountRequest {

    @NotNull
    private String username;

    private Currency currency;
}

