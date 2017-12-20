package com.github.viettd.exchange_rates.storage;

import com.github.viettd.exchange_rates.bean.ExchangeRateListResponse;
import com.github.viettd.exchange_rates.common.Config;
import com.github.viettd.exchange_rates.utils.Utils;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;

public class ExchangeRatesStorage {
    private static ExchangeRateListResponse exchangeRates;

    public static ExchangeRateListResponse getExchangeRates() {
        if (exchangeRates == null) {
            loadExchangeRates();
        }
        return exchangeRates;
    }

    private static void loadExchangeRates() {
        String url = String.format(Config.getSourceUrl(), Config.getSourceAppId());
        String ratesResp = Utils.callGetApi(url);
        if (StringUtils.isNotBlank(ratesResp)) {
            exchangeRates = new Gson().fromJson(ratesResp, ExchangeRateListResponse.class);
        }
    }


}
