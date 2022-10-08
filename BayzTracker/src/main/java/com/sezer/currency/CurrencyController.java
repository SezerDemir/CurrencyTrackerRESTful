package com.sezer.currency;

import com.sezer.exception.UnsupportedCurrencyCreationException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.InvalidParameterException;
import java.util.NoSuchElementException;

@RestController @Data @Slf4j
@RequestMapping(path = "api/currency")
public class CurrencyController {
    private final CurrencyMapper currencyMapper;
    private final CurrencyService currencyService;

    @PostMapping(value = "/add")
    public ResponseEntity<?> addCurrency(@RequestBody CurrencyDTO currencyDTO) {
        try {
            if(currencyDTO != null) {
                currencyService.addCurrency(currencyMapper.currencyDTOToCurrency(currencyDTO));
            } else {
                String message = new InvalidParameterException().getMessage();
                log.error(message);
                return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
            }
        }catch (UnsupportedCurrencyCreationException e) {
            log.error(e.toString());
            return new ResponseEntity<>(currencyDTO, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(currencyDTO, HttpStatus.CREATED);
    }

    @PatchMapping(value = "/update")
    public ResponseEntity<?> updateCurrency(@RequestBody CurrencyDTO currencyDTO) {
        Currency currency;
        if(currencyDTO != null && !currencyDTO.getCurrencySymbol().strip().equals("") && currencyDTO.getCurrentPrice() > 0.0f) {
            currency = currencyService.updateCurrency(currencyDTO.getCurrencySymbol(), currencyDTO.getCurrentPrice());
        } else {
            String message = new InvalidParameterException().getMessage();
            log.error(message);
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(currencyMapper.currencyToCurrencyDTO(currency), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> deleteCurrency(@RequestParam("symbol") String symbol) {
        if(symbol != null && !symbol.strip().equals("")) {
            try{
                currencyService.deleteCurrency(symbol);
            } catch (NoSuchElementException e) {
                log.error(e.toString());
                return new ResponseEntity<>(e.toString(), HttpStatus.NOT_FOUND);
            }
        } else {
            String message = new InvalidParameterException().getMessage();
            log.error(message);
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("DELETED", HttpStatus.OK);
    }

    @GetMapping(value = "/get")
    public ResponseEntity<CurrencyDTO> getCurrency(@RequestParam("symbol") String symbol) {
        Currency currency = null;
        if(symbol != null && !symbol.strip().equals("")) {
            try {
                currency = currencyService.getCurrency(symbol);
            } catch (NoSuchElementException e ) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(currencyMapper.currencyToCurrencyDTO(currency),HttpStatus.OK);
    }
}
