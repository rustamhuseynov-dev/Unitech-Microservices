package com.rustam.ms_account.service;

import com.rustam.ms_account.config.WebClientConfig;
import com.rustam.ms_account.dao.entity.Account;
import com.rustam.ms_account.dao.enums.AccountStatus;
import com.rustam.ms_account.dao.repository.AccountRepository;
import com.rustam.ms_account.dto.request.*;
import com.rustam.ms_account.dto.response.AccountCurrencyConvertResponse;
import com.rustam.ms_account.dto.response.AccountIncreaseResponse;
import com.rustam.ms_account.dto.response.AccountResponse;
import com.rustam.ms_account.exception.custom.ResponseNotFoundException;
import com.rustam.ms_account.mapper.AccountMapper;
import com.rustam.ms_account.util.IbanService;
import com.rustam.ms_account.util.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;

    private final IbanService ibanService;

    private final RestTemplate restTemplate;

    private final JwtUtil jwtUtil;

    private final WebClient.Builder builder;

   // @Value("${spring.application.ms-auth}")
    private final static String AUTH_SERVICE_URL = "http://localhost:8085/api/v1/user/read";

    @Value("${spring.application.ms-currency}")
    private String CURRENCY_SERVICE_URL;

    @Transactional
    public AccountResponse createAccount(AccountRequest accountRequest, String token) {

        UUID userId = securityContextHolder(token);

        String url = AUTH_SERVICE_URL + "/" + userId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<AccountResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, AccountResponse.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ResponseNotFoundException("Failed to fetch user data from MS-Auth");
        }
        AccountResponse accountResponse = response.getBody();

        Account account = Account.builder()
                .currency(accountRequest.getCurrency())
                .iban(ibanService.generateIbanForUser())
                .username(accountResponse.getUsername())
                .status(AccountStatus.ACTIVATED)
                .customerId(userId)
                .build();
        accountRepository.save(account);
        return accountMapper.toDto(account);
    }

    private UUID securityContextHolder(String token) {

        token = token.replace("Bearer ", "");

        UUID userId = UUID.fromString(jwtUtil.extractUsername(token));

        log.info("token {}", userId);

        return userId;

    }

    public List<Account> readAssets() {
        return accountRepository.findAllActivatedAccounts();
    }

    public List<Account> readAssetsByUsername(String username) {
        return accountRepository.findAllActivatedAccountsAndUsername(username);
    }

    public AccountResponse findByUsernameAndIbanAndStatus(String username, String iban, AccountStatus status) {
        return accountRepository.findByUsernameAndIbanAndStatus(username, iban, status)
                .map(accountMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Account with iban " + iban + " doesn't exists!"));
    }

    public AccountResponse findByIban(String iban) {
        return accountRepository.findByIban(iban)
                .map(accountMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Account with iban " + iban + " doesn't exists!"));
    }

    public Account findByAccountIban(String iban) {
        return accountRepository.findByIban(iban)
                .orElseThrow(() -> new RuntimeException("Account with iban " + iban + " doesn't exists!"));
    }

    @Transactional
    public AccountResponse updateAccount(UpdateAccountRequest updateAccountRequest) {
        Account account = accountRepository.findByUsername(updateAccountRequest.getUsername());
        account.setCurrency(updateAccountRequest.getCurrency());
        accountRepository.save(account);
        return accountMapper.toDto(account);
    }

    @Transactional
    public AccountIncreaseResponse accountIncrease(AccountIncreaseRequest accountIncreaseRequest) {
        Account account = findByAccountIban(accountIncreaseRequest.getIban());
        BigDecimal sum = (account.getBalance() != null ? account.getBalance() : BigDecimal.ZERO)
                .add(accountIncreaseRequest.getBalance());
        account.setBalance(sum);
        account.setIncreaseBalanceAt(LocalDateTime.now());
        accountRepository.save(account);
        return accountMapper.toIncreaseResponse(account);
    }

    @Transactional
    public AccountCurrencyConvertResponse accountCurrencyConvert(AccountCurrencyConvertRequest accountCurrencyConvertRequest, String token) {
        UUID userId = securityContextHolder(token);
        Account account = accountRepository.findByCustomerId(userId).
                orElseThrow(() -> new ResponseNotFoundException("No such customer was found."));
        if (account.getCurrency().equals(accountCurrencyConvertRequest.getCurrencyCode())){
            throw new ResponseNotFoundException("The currency of this employee is the same as the currency to be converted.");
        }
        accountCurrencyConvertRequest.setBaseCurrency(account.getCurrency());
        BigDecimal calculatedValue =
                getCurrencyValue(accountCurrencyConvertRequest)
                .map(currency ->
                        account.getBalance().multiply(BigDecimal.valueOf(currency)))
                .block();
        account.setCurrencyExchangeSum(calculatedValue);
        accountRepository.save(account);
        return AccountCurrencyConvertResponse.builder()
                .currentCurrency(account.getCurrency())
                .convertCurrency(accountCurrencyConvertRequest.getCurrencyCode())
                .sum(calculatedValue)
                .build();
    }

    private Mono<Double> getCurrencyValue(AccountCurrencyConvertRequest accountCurrencyConvertRequest) {
        return builder.build()
                .post() // POST metodu istifadə edilir
                .uri(CURRENCY_SERVICE_URL) // URL sadəcə endpoint-i göstərir
                .body(Mono.just(accountCurrencyConvertRequest), AccountCurrencyConvertRequest.class) // Request body əlavə edilir
                .retrieve() // Cavab alınır
                .bodyToMono(Double.class) // Double formatında cavab gözlənilir
                .defaultIfEmpty(0.0) // Əgər heç bir nəticə yoxdursa, default dəyər
                .onErrorResume(e -> {
                    log.error("Error occurred while fetching currency value: {}", e.getMessage());
                    return Mono.just(0.0); // Default error dəyər
                });
    }

    @Transactional
    public AccountIncreaseResponse accountWithdraw(AccountIncreaseRequest accountIncreaseRequest) {
        Account account = findByAccountIban(accountIncreaseRequest.getIban());
        BigDecimal sum = account.getBalance().subtract(accountIncreaseRequest.getBalance());
        account.setBalance(sum);
        account.setIncreaseBalanceAt(LocalDateTime.now());
        accountRepository.save(account);
        return accountMapper.toIncreaseResponse(account);
    }

    @Transactional
    public void accountUpdated(AccountResponse accountResponse) {
        if (accountResponse.getId() == null) {
            throw new RuntimeException("id not exists");
        }
        Account account = accountMapper.toEntity(accountResponse);
        accountRepository.save(account);
    }
}
