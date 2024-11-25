package com.rustam.ms_account.service;

import com.rustam.ms_account.dao.entity.TransferHistory;
import com.rustam.ms_account.dao.enums.AccountStatus;
import com.rustam.ms_account.dao.repository.TransferHistoryRepository;
import com.rustam.ms_account.dto.request.TransferRequest;
import com.rustam.ms_account.dto.response.AccountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransferService {

    private final TransferHistoryRepository transferHistoryRepository;

    private final AccountService accountService;

    @Transactional
    public String cartToCart(TransferRequest transferRequest) {
        AccountResponse account = accountService.findByIban(transferRequest.getSenderIban());
        AccountResponse accountDto = accountService.findByUsernameAndIbanAndStatus(account.getUsername(), transferRequest.getSenderIban(), AccountStatus.ACTIVATED);
        BigDecimal senderBalance = accountDto.getBalance();
        if (senderBalance.compareTo(transferRequest.getAmount()) < 0){
            throw new RuntimeException("Invalid amount!");
        }
        AccountResponse receiverAccount = accountService.findByIban(transferRequest.getReceiverIban());
        validateReceiverAccount(account, receiverAccount);

        accountDto.setBalance(senderBalance.subtract(transferRequest.getAmount()));
        accountService.accountUpdated(accountDto);

        receiverAccount.setBalance(receiverAccount.getBalance().add(transferRequest.getAmount()));
        accountService.accountUpdated(receiverAccount);
        TransferHistory transferHistory = TransferHistory.builder()
                .senderIban(accountDto.getIban())
                .receiverIban(receiverAccount.getIban())
                .amount(transferRequest.getAmount())
                .build();
        transferHistoryRepository.save(transferHistory);
        return "Cart to Cart transfer completed successfully.";
    }


    private void validateReceiverAccount(AccountResponse senderAccount, AccountResponse receiverAccount) {
        if (receiverAccount.getStatus() == AccountStatus.DEACTIVATED)
            throw new RuntimeException("Transfer into deactivated account not permitted!");
        if (receiverAccount.getIban().equals(senderAccount.getIban()))
            throw new RuntimeException("Transfer into the same account not permitted!");
    }
}
