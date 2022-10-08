package com.sezer.notification;

import com.sezer.alert.Alert;
import com.sezer.alert.AlertRepository;
import com.sezer.currency.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import com.sezer.currency.Currency;
import java.util.List;

@Component @EnableScheduling @Slf4j @RequiredArgsConstructor
public class NotificationManager {
    private final AlertRepository alertRepository;
    private final CurrencyRepository currencyRepository;
    private List<Currency> currencyList = new ArrayList<>();
    private List<Alert> alertCheckList = new ArrayList<>();
    private List<Alert> alertList = new ArrayList<>();

    @Scheduled(fixedRate = 30_000)
    public void startNotificationTask() {
        alertRepository.findAll().forEach(e -> alertList.add(e));
        if(!alertList.isEmpty()) {
            alertList.forEach( e -> {
                if(e.getStatus() == Alert.AlertStatus.TRIGGERED) {
                    log.info("Alarm id of " + e.getId() +  "! Target price of " + e.getTargetPrice() +  " is reached "
                                    + "for currency of " + e.getCurrency()
                            );
                }
            });
            alertList.clear();
        }
    }

    @Scheduled(fixedRate = 5_000)
    public void checkAlertStatus() {
       currencyRepository.findAll().forEach(e -> currencyList.add(e));
       if(!currencyList.isEmpty()) {
           alertRepository.findAll().forEach(e -> alertCheckList.add(e));
           if(!alertCheckList.isEmpty()) {
               alertCheckList.forEach(a -> currencyList.stream().forEach(
                       c -> {
                           if(a.getCurrency().equals(c.getSymbol())) {
                               if(a.getTargetPrice() <= c.getCurrentPrice() &&
                                       a.getStatus() == Alert.AlertStatus.NEW) {
                                   a.setStatus(Alert.AlertStatus.TRIGGERED);
                                   alertRepository.save(a);
                               } else if((a.getTargetPrice() > c.getCurrentPrice())
                                       && (a.getStatus() != Alert.AlertStatus.NEW && a.getStatus() != Alert.AlertStatus.CANCELED)) {
                                   a.setStatus(Alert.AlertStatus.NEW);
                                   alertRepository.save(a);
                               }
                           }
                       }
               ));
           }
           currencyList.clear();
           alertCheckList.clear();
       }
    }


}
