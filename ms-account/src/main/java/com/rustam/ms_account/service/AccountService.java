package com.rustam.ms_account.service;

import com.rustam.ms_account.dao.entity.Account;
import com.rustam.ms_account.dao.enums.AccountStatus;
import com.rustam.ms_account.dao.repository.AccountRepository;
import com.rustam.ms_account.dto.request.AccountIncreaseRequest;
import com.rustam.ms_account.dto.request.AccountRequest;
import com.rustam.ms_account.dto.response.AccountIncreaseResponse;
import com.rustam.ms_account.dto.response.AccountResponse;
import com.rustam.ms_account.mapper.AccountMapper;
import com.rustam.ms_account.util.IbanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;

    private final IbanService ibanService;

    @Transactional
    public AccountResponse createAccount(AccountRequest accountRequest) {

        Account account = Account.builder()
                .username(accountRequest.getUsername())
                .currency(accountRequest.getCurrency())
                .iban(ibanService.generateIbanForUser())
                .status(AccountStatus.ACTIVATED)
                .build();
        accountRepository.save(account);
        return accountMapper.toDto(account);
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
    public AccountResponse updateAccount(AccountRequest accountRequest) {
        Account account = accountRepository.findByUsername(accountRequest.getUsername());
        account.setCurrency(accountRequest.getCurrency());
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
