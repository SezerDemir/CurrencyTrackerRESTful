package com.sezer.alert;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AlertMapper {
    @Mapping(source = "currencySymbol", target = "currency")
    @Mapping(source = "currencyTargetPrice", target = "targetPrice")
    @Mapping(source = "alertStatus", target = "status")
    public Alert alertDTOToAlert(AlertDTO alertDTO);
    @Mapping(source = "currency", target = "currencySymbol")
    @Mapping(source = "targetPrice", target = "currencyTargetPrice")
    @Mapping(source = "status", target = "alertStatus")
    @Mapping(source = "id", target = "id")
    public AlertDTO alertToAlertDTO(Alert alert);
}
