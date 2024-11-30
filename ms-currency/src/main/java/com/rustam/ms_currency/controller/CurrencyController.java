package com.rustam.ms_currency.controller;

import com.rustam.ms_currency.dto.CurrencyDto;
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

    @GetMapping()
    public ResponseEntity<Object> read(@RequestParam String currencyCode){
        return new ResponseEntity<>(currencyService.getCurrency(currencyCode),HttpStatus.OK);
    }
}
