package com.sezer.currency;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends PagingAndSortingRepository<Currency, Long> {
    Currency getBySymbol(String symbol);
}
