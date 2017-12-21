package storage;

import com.github.viettd.exchange_rates.bean.ExchangeRateListResponse;
import com.github.viettd.exchange_rates.common.Config;
import com.github.viettd.exchange_rates.storage.ExchangeRatesStorage;
import com.github.viettd.exchange_rates.utils.Utils;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RunWith(PowerMockRunner.class)
public class ExchangeRatesStorageTest {

    @Test
    @PrepareForTest(Utils.class)
    public void test_loadExchangeRates_should_not_reload_exchange_rates_when_error_occur() {
        ExchangeRateListResponse oldRates = ExchangeRatesStorage.getExchangeRates();
        ExchangeRatesStorage.loadExchangeRates();
        Assert.assertEquals(oldRates, ExchangeRatesStorage.getExchangeRates());
    }

    @Test
    @PrepareForTest({Utils.class, Config.class})
    public void test_loadExchangeRates_should_not_reload_exchange_rates_when_cannot_get_response_from_source() {
        String url = "http://abc.com";

        PowerMockito.mockStatic(Config.class);
        PowerMockito.when(Config.getSourceUrl()).thenReturn(url);

        PowerMockito.mockStatic(Utils.class);
        PowerMockito.when(Utils.callGetApi(url)).thenReturn("");
        ExchangeRateListResponse oldRates = ExchangeRatesStorage.getExchangeRates();
        ExchangeRatesStorage.loadExchangeRates();
        Assert.assertEquals(oldRates, ExchangeRatesStorage.getExchangeRates());
    }

    @Test
    @PrepareForTest({Utils.class, Config.class})
    public void test_loadExchangeRates_should_reload_exchange_rates_when_get_a_valid_response_from_source() {
        ExchangeRateListResponse ratesListResp = new ExchangeRateListResponse();
        ratesListResp.setBase("USD");
        ratesListResp.setTimestamp(Instant.now().getEpochSecond());
        Map<String, BigDecimal> rates = new HashMap<>();
        ratesListResp.setRates(rates);

        String json = new Gson().toJson(ratesListResp);

        String url = "http://abc.com";

        PowerMockito.mockStatic(Config.class);
        PowerMockito.when(Config.getSourceUrl()).thenReturn(url);


        PowerMockito.mockStatic(Utils.class);
        PowerMockito.when(Utils.callGetApi(url)).thenReturn(json);

        ExchangeRateListResponse oldRates = ExchangeRatesStorage.getExchangeRates();
        ExchangeRatesStorage.loadExchangeRates();
        ExchangeRateListResponse newRates = ExchangeRatesStorage.getExchangeRates();

//        Assert.assertNotEquals(oldRates, newRates);
        Assert.assertEquals(newRates.getRates(), ratesListResp.getRates());
        Assert.assertEquals(newRates.getBase(), ratesListResp.getBase());
        Assert.assertEquals(newRates.getTimestamp(), ratesListResp.getTimestamp());
    }

}
