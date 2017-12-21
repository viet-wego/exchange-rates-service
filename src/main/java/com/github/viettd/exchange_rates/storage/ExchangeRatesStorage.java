package com.github.viettd.exchange_rates.storage;

import com.github.viettd.exchange_rates.bean.ExchangeRateListResponse;
import com.github.viettd.exchange_rates.common.Config;
import com.github.viettd.exchange_rates.utils.Utils;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;

import java.time.Instant;

public class ExchangeRatesStorage {
    private static ExchangeRateListResponse exchangeRates = new ExchangeRateListResponse();

    public static ExchangeRateListResponse getExchangeRates() {
        if (exchangeRates == null) {
            loadExchangeRates();
        }
        return exchangeRates;
    }

    public static void loadExchangeRates() {
        if (exchangeRates == null || exchangeRates.getRates() == null || exchangeRates.getRates().isEmpty()
                || Instant.now().minusSeconds(3600).getEpochSecond() >= exchangeRates.getTimestamp())
            try {
                String url = String.format(Config.getSourceUrl(), Config.getSourceAppId());
                String ratesResp = Utils.callGetApi(url);
                if (StringUtils.isNotBlank(ratesResp)) {
                    synchronized (exchangeRates) {
                        ExchangeRateListResponse response = new Gson().fromJson(ratesResp, ExchangeRateListResponse.class);
                        exchangeRates.setTimestamp(response.getTimestamp());
                        exchangeRates.setBase(response.getBase());
                        exchangeRates.setRates(response.getRates());
                    }
                }
            } catch (Exception e) {
            }
    }

}
