package com.rustam.ms_account.controller;

import com.rustam.ms_account.dao.entity.Account;
import com.rustam.ms_account.dto.request.AccountIncreaseRequest;
import com.rustam.ms_account.dto.request.AccountRequest;
import com.rustam.ms_account.dto.response.AccountIncreaseResponse;
import com.rustam.ms_account.dto.response.AccountResponse;
import com.rustam.ms_account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping(path = "/create-account")
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountRequest accountRequest){
        return new ResponseEntity<>(accountService.createAccount(accountRequest), HttpStatus.CREATED);
    }

    @GetMapping(path = "/read-assets")
    public ResponseEntity<List<Account>> readAssets(){
        return new ResponseEntity<>(accountService.readAssets(), HttpStatus.OK);
    }

    @GetMapping(path = "/read-assets/{username}")
    public ResponseEntity<List<Account>> readAssetsByUsername(@PathVariable String username){
        return new ResponseEntity<>(accountService.readAssetsByUsername(username), HttpStatus.OK);
    }

    @PutMapping(path = "/update-account")
    public ResponseEntity<AccountResponse> updateAccount(@RequestBody AccountRequest accountRequest){
        return new ResponseEntity<>(accountService.updateAccount(accountRequest),HttpStatus.OK);
    }

    @PostMapping(path = "/account-increase")
    public ResponseEntity<AccountIncreaseResponse> accountIncrease(@RequestBody AccountIncreaseRequest accountIncreaseRequest){
        return new ResponseEntity<>(accountService.accountIncrease(accountIncreaseRequest),HttpStatus.OK);
    }

    @PostMapping(path = "/account-withdraw")
    public ResponseEntity<AccountIncreaseResponse> accountWithdraw(@RequestBody AccountIncreaseRequest accountIncreaseRequest){
        return new ResponseEntity<>(accountService.accountWithdraw(accountIncreaseRequest),HttpStatus.OK);
    }

}
