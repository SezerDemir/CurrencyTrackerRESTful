package com.sezer.currency;


import com.sezer.exception.UnsupportedCurrencyCreationException;
import lombok.Data;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;


@Data
@Service
public class CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final List<String> unsupportedCurrencies = Arrays.asList();
    private final CurrencyMapper currencyMapper;
    public void addCurrency(Currency currency) throws UnsupportedCurrencyCreationException {
        if(Arrays.stream(UnsupportedCurrencies.values()).anyMatch(e -> e.toString().equals(currency.getSymbol()))) {
            throw new UnsupportedCurrencyCreationException("Unsupported currency:" + currency.getSymbol());
        }
        currencyMapper.currencyToCurrencyDTO(currencyRepository.save(currency));
    }
    public Currency updateCurrency(String symbol, float price) {
        Currency currency = currencyRepository.getBySymbol(symbol);
        if(currency == null) {
            throw new NoSuchElementException("There is no such currency in database: " + symbol);
        } else {
            currency.setCurrentPrice(price);
            currency = currencyRepository.save(currency);
        }
        return currency;
    }
    public Currency getCurrency(String symbol) {
        Currency currency = currencyRepository.getBySymbol(symbol);
        if(currency == null) {
            throw new NoSuchElementException("There is no such currency in database: " + symbol);
        }
        return currency;
    }
    public void deleteCurrency(String symbol) {
        Currency currency = currencyRepository.getBySymbol(symbol);
        if(currency == null) {
            throw new NoSuchElementException("There is no such currency in database: " + symbol);
        }
        currencyRepository.delete(currency);
    }

    public enum UnsupportedCurrencies {
        ETH, LTC, ZKN, MRD, LPR, GBZ
    }
}
