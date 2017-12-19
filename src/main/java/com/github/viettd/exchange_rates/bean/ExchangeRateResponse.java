package com.github.viettd.exchange_rates.bean;

import java.math.BigDecimal;
import java.util.Map;

public class ExchangeRateResponse extends BaseExchangeRateResponse {
    private Map<String, BigDecimal> rate;

    public Map<String, BigDecimal> getRate() {
        return rate;
    }

    public void setRate(Map<String, BigDecimal> rate) {
        this.rate = rate;
    }
}
