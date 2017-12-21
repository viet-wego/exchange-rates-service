package com.github.viettd.exchange_rates.storage;

import com.github.viettd.exchange_rates.bean.ExchangeRateListResponse;
import com.github.viettd.exchange_rates.common.Config;
import com.github.viettd.exchange_rates.utils.Utils;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;



public class ExchangeRatesStorage {

    private static final Logger LOGGER = Logger.getLogger(ExchangeRatesStorage.class);

    private static ExchangeRateListResponse exchangeRates = new ExchangeRateListResponse();

    public static void loadExchangeRates() {
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
            LOGGER.error("Cannot load exchange rates from source", e);
        }
    }

    public static ExchangeRateListResponse getExchangeRates() {
        return exchangeRates;
    }

}
