package com.sezer.currency;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class CurrencyDTO {
    private String currencyName;
    private String currencySymbol;
    private float currentPrice;
}
