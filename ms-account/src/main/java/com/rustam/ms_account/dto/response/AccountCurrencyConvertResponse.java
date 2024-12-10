package com.rustam.ms_account.dto.response;

import com.rustam.ms_account.dao.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountCurrencyConvertResponse {

    private Currency currentCurrency,convertCurrency;

    private BigDecimal sum;
}
