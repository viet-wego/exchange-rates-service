package com.github.viettd.exchange_rates.bean;

import java.math.BigDecimal;
import java.util.Map;

public class ExchangeRatesResponse extends BaseExchangeRateResponse {
    private Map<String, BigDecimal> rates;

    public Map<String, BigDecimal> getRates() {
        return rates;
    }

    public void setRates(Map<String, BigDecimal> rates) {
        this.rates = rates;
    }
}
