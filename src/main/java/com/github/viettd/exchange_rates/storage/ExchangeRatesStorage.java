package com.github.viettd.exchange_rates.storage;

import com.github.viettd.exchange_rates.bean.ExchangeRatesResponse;

public class ExchangeRatesStorage {
    private static ExchangeRatesResponse exchangeRates;

    public static ExchangeRatesResponse getExchangeRates() {
        return exchangeRates;
    }
}
