package com.rustam.ms_account.dto.request;

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
public class AccountCurrencyConvertRequest {

    private Currency currencyCode;

    private Currency baseCurrency;

}
