package com.sezer.currency;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {

    @Mapping(source = "name", target = "currencyName")
    @Mapping(source = "symbol", target = "currencySymbol")
    CurrencyDTO currencyToCurrencyDTO(Currency book);
    @Mapping(source = "currencyName", target = "name")
    @Mapping(source = "currencySymbol", target = "symbol")
    Currency currencyDTOToCurrency(CurrencyDTO book);

}
