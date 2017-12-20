package com.github.viettd.exchange_rates.service;

import com.github.viettd.exchange_rates.bean.ExchangeRateListResponse;
import com.github.viettd.exchange_rates.bean.ExchangeRateResponse;
import com.github.viettd.exchange_rates.bean.Response;
import com.github.viettd.exchange_rates.storage.ExchangeRatesStorage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.github.viettd.exchange_rates.common.Constant.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ExchangeRatesStorage.class)
public class ExchangeRatesServiceTest {

    private static final String BASE_CURRENCY = "USD";
    private static final String CURRENCY = "SGD";
    private static final long TIMESTAMP = 123456L;
    private static final BigDecimal RATE = BigDecimal.TEN;

    private ExchangeRateListResponse nullRateListResp;
    private Response successfulRateListResponse;
    private Response failRateResponse;
    private ExchangeRateListResponse emptyRateListResp;
    private ExchangeRateResponse sgdRateResponse;
    private Response successfulSgdRateResponse;
    private ExchangeRateListResponse sgdRateListResp;

    @Before
    public void setUp() {
        nullRateListResp = new ExchangeRateListResponse();

        successfulRateListResponse = new Response();
        successfulRateListResponse.setCode(CODE_SUCCESS);
        successfulRateListResponse.setMessage(MSG_SUCCESS);
        successfulRateListResponse.setData(nullRateListResp);

        failRateResponse = new Response();

        emptyRateListResp = new ExchangeRateListResponse();
        emptyRateListResp.setRates(new HashMap<>());

        sgdRateResponse = new ExchangeRateResponse();
        sgdRateResponse.setTimestamp(TIMESTAMP);
        sgdRateResponse.setBase(BASE_CURRENCY);
        Map<String, BigDecimal> rate = new HashMap<>();
        rate.put(CURRENCY, RATE);
        sgdRateResponse.setRate(rate);

        successfulSgdRateResponse = new Response();
        successfulSgdRateResponse.setCode(CODE_SUCCESS);
        successfulSgdRateResponse.setMessage(MSG_SUCCESS);
        successfulSgdRateResponse.setData(sgdRateResponse);

        sgdRateListResp = new ExchangeRateListResponse();
        sgdRateListResp.setBase(BASE_CURRENCY);
        sgdRateListResp.setTimestamp(TIMESTAMP);
        sgdRateListResp.setRates(rate);
    }

    @After
    public void tearDown() {
        failRateResponse = null;

        successfulRateListResponse = null;
        nullRateListResp = null;
        emptyRateListResp = null;

        sgdRateListResp = null;
        successfulSgdRateResponse = null;
        sgdRateResponse = null;
    }

    @Test
    public void test_getExchangeRates_should_return_success_response() {
        PowerMockito.mockStatic(ExchangeRatesStorage.class);
        PowerMockito.when(ExchangeRatesStorage.getExchangeRates()).thenReturn(nullRateListResp);
        Response response = ExchangeRatesService.getExchangeRates();
        Assert.assertEquals(successfulRateListResponse.getCode(), response.getCode());
        Assert.assertEquals(successfulRateListResponse.getMessage(), response.getMessage());
        Assert.assertEquals(successfulRateListResponse.getData(), response.getData());
    }

    @Test
    public void test_getExchangeRate_should_return_fail_response_when_cached_rate_list_response_is_null() {
        PowerMockito.mockStatic(ExchangeRatesStorage.class);
        PowerMockito.when(ExchangeRatesStorage.getExchangeRates()).thenReturn(null);
        Response response = ExchangeRatesService.getExchangeRate(CURRENCY);
        Assert.assertEquals(failRateResponse.getCode(), response.getCode());
        Assert.assertEquals(failRateResponse.getMessage(), response.getMessage());
        Assert.assertNull(response.getData());
    }

    @Test
    public void test_getExchangeRate_should_return_fail_response_when_rate_list_is_null() {
        PowerMockito.mockStatic(ExchangeRatesStorage.class);
        PowerMockito.when(ExchangeRatesStorage.getExchangeRates()).thenReturn(nullRateListResp);
        Response response = ExchangeRatesService.getExchangeRate(CURRENCY);
        Assert.assertEquals(failRateResponse.getCode(), response.getCode());
        Assert.assertEquals(failRateResponse.getMessage(), response.getMessage());
        Assert.assertNull(response.getData());
    }

    @Test
    public void test_getExchangeRate_should_return_fail_response_when_rate_list_is_empty() {
        PowerMockito.mockStatic(ExchangeRatesStorage.class);
        PowerMockito.when(ExchangeRatesStorage.getExchangeRates()).thenReturn(emptyRateListResp);
        Response response = ExchangeRatesService.getExchangeRate(CURRENCY);
        Assert.assertEquals(failRateResponse.getCode(), response.getCode());
        Assert.assertEquals(failRateResponse.getMessage(), response.getMessage());
        Assert.assertNull(response.getData());
    }

    @Test
    public void test_getExchangeRate_should_return_fail_response_when_rate_is_not_found() {
        PowerMockito.mockStatic(ExchangeRatesStorage.class);
        PowerMockito.when(ExchangeRatesStorage.getExchangeRates()).thenReturn(sgdRateListResp);
        Response response = ExchangeRatesService.getExchangeRate("VND");
        Assert.assertEquals(failRateResponse.getCode(), response.getCode());
        Assert.assertEquals(failRateResponse.getMessage(), response.getMessage());
        Assert.assertNull(response.getData());
    }

    @Test
    public void test_getExchangeRate_should_return_successful_response_when_rate_is_found() {
        PowerMockito.mockStatic(ExchangeRatesStorage.class);
        PowerMockito.when(ExchangeRatesStorage.getExchangeRates()).thenReturn(sgdRateListResp);
        Response response = ExchangeRatesService.getExchangeRate(CURRENCY);
        Assert.assertEquals(successfulSgdRateResponse.getCode(), response.getCode());
        Assert.assertEquals(successfulSgdRateResponse.getMessage(), response.getMessage());
        ExchangeRateResponse data = (ExchangeRateResponse) response.getData();
        Assert.assertEquals(sgdRateResponse.getBase(), data.getBase());
        Assert.assertEquals(sgdRateResponse.getTimestamp(), data.getTimestamp());
        Assert.assertEquals(sgdRateResponse.getRate(), data.getRate());

    }
}
