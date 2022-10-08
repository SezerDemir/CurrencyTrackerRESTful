package com.sezer.alert;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    Alert getByTargetPrice(float targetPrice);
    List<Alert> getByCurrency(String symbol);
}
