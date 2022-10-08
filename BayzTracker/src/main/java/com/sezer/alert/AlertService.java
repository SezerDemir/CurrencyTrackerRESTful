package com.sezer.alert;

import com.sezer.currency.Currency;
import com.sezer.currency.CurrencyRepository;
import com.sezer.exception.UnsupportedCurrencyCreationException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Service @Data @Slf4j
public class AlertService {
    private final AlertRepository alertRepository;
    private final CurrencyRepository currencyRepository;
    public Alert addAlert(Alert alert) throws UnsupportedCurrencyCreationException {
        Currency currency = currencyRepository.getBySymbol(alert.getCurrency());
        if(currency == null) {
            throw new NoSuchElementException("There is no such currency in the system");
        }
        try {
            if(currency.getCurrentPrice() < alert.getTargetPrice()) {
                alert.setStatus(Alert.AlertStatus.NEW);
            } else {
                alert.setStatus(Alert.AlertStatus.TRIGGERED);
            }
            alert = alertRepository.save(alert);
        } catch (Exception e) {
            throw new DuplicateKeyException(e.getMessage());
        }
        return alert;
    }
    public void removeAlert(Long id) {
        try {
            alertRepository.deleteById(id);
        } catch (Exception e) {
            throw e;
        }

    }
    public List<Alert> getAllAlerts() {
        return alertRepository.findAll();
    }
    public List<Alert> getBySymbol(String symbol) {
        return alertRepository.getByCurrency(symbol);
    }

    public Alert update(Long id, float targetPrice) {
        Alert alert = alertRepository.findById(id).orElse(null);
        if(alert == null) {
            throw new NoSuchElementException();
        }
        Currency currency = currencyRepository.getBySymbol(alert.getCurrency());
        alert.setTargetPrice(targetPrice);
        if(alert.getStatus() != Alert.AlertStatus.CANCELED) {
        	if(alert.getStatus() != Alert.AlertStatus.CANCELED && alert.getTargetPrice() <= currency.getCurrentPrice()) {
                alert.setStatus(Alert.AlertStatus.TRIGGERED);
            } else {
                alert.setStatus(Alert.AlertStatus.NEW);
            }
        }
        try {
            alert = alertRepository.save(alert);
        } catch (Exception e) {
            throw e;
        }

        return alert;
    }

    public void cancel(Long id) throws Exception {
        Alert alert = alertRepository.findById(id).orElse(null);
        if(alert == null) {
            throw new NoSuchElementException();
        }
        if(alert.getStatus() != Alert.AlertStatus.TRIGGERED) {
            alert.setStatus(Alert.AlertStatus.CANCELED);
            alertRepository.save(alert);
        } else  {
            throw new Exception("Alarm already triggered, you can only acknowledge it");
        }
    }

    public void acknowledge(Long id) throws Exception {
        Alert alert = alertRepository.findById(id).orElse(null);
        if(alert == null) {
            throw new NoSuchElementException();
        }
        if(alert.getStatus() == Alert.AlertStatus.TRIGGERED) {
            alert.setStatus(Alert.AlertStatus.ACKED);
            alertRepository.save(alert);
        } else  {
            throw new Exception("Alarm is not triggered, you can only acknowledge it after it is triggered");
        }
    }
}
