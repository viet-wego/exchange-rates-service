package com.github.viettd.exchange_rates.service;

import com.github.viettd.exchange_rates.bean.ExchangeRateResponse;
import com.github.viettd.exchange_rates.bean.Response;
import com.github.viettd.exchange_rates.storage.ExchangeRatesStorage;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.github.viettd.exchange_rates.common.Constant.*;


public class ExchangeRatesService {

    public static Response getExchangeRates() {
        Response response = new Response();
        response.setCode(CODE_SUCCESS);
        response.setMessage(MSG_SUCCESS);
        response.setData(ExchangeRatesStorage.getExchangeRates());
        return response;
    }

    public static Response getExchangeRate(String currency) {
        Response response = new Response();
        BigDecimal rateValue = ExchangeRatesStorage.getExchangeRates().getRates().get(currency);

        if (rateValue != null) {
            Map<String, BigDecimal> rate = new HashMap<>();
            rate.put(currency, rateValue);

            ExchangeRateResponse rateResponse = new ExchangeRateResponse();
            rateResponse.setBase(ExchangeRatesStorage.getExchangeRates().getBase());
            rateResponse.setTimestamp(ExchangeRatesStorage.getExchangeRates().getTimestamp());
            rateResponse.setRate(rate);

            response.setCode(CODE_SUCCESS);
            response.setMessage(MSG_SUCCESS);
            response.setData(rateResponse);
        }

        return response;
    }
}
