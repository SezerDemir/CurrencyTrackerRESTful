package com.sezer.alert;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Builder @Data
public class AlertDTO {
    @NotNull
    private String currencySymbol;
    @NotNull
    private Float currencyTargetPrice;
    private Alert.AlertStatus alertStatus;
    private Long id;
}
