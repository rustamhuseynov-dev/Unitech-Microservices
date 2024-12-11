package com.rustam.ms_currency.controller;

import com.rustam.ms_currency.dto.CurrencyDto;
import com.rustam.ms_currency.dto.CurrencyRequest;
import com.rustam.ms_currency.service.CurrencyService;
import jakarta.ws.rs.GET;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/currency")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class CurrencyController {

    CurrencyService currencyService;

    @GetMapping(path = "/currencies")
    public ResponseEntity<Map<Object,Object>> readAll(){
        return new ResponseEntity<>(currencyService.getAllCurrencies(),HttpStatus.ACCEPTED);
    }

    @PostMapping
    public ResponseEntity<Object> read(@RequestBody CurrencyRequest currencyRequest){
        return new ResponseEntity<>(currencyService.getCurrency(currencyRequest),HttpStatus.OK);
    }

    @PostMapping(path = "/save")
    public void save(){
        currencyService.save();
    }
}
