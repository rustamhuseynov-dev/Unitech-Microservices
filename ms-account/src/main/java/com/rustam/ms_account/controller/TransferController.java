package com.rustam.ms_account.controller;


import com.rustam.ms_account.dto.request.TransferRequest;
import com.rustam.ms_account.dto.response.TransferResponse;
import com.rustam.ms_account.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/transfer")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping(path = "/cart-to-cart")
    private ResponseEntity<String> cartToCart(@RequestBody TransferRequest transferRequest){
        return new ResponseEntity<>(transferService.cartToCart(transferRequest), HttpStatus.OK);
    }
}
