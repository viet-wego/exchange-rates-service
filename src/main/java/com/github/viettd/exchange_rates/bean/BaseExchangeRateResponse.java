package com.github.viettd.exchange_rates.bean;

public class BaseExchangeRateResponse {
    protected long timestamp;
    protected String base;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }
}
