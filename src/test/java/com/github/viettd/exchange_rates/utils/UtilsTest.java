package com.github.viettd.exchange_rates.utils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class UtilsTest {

    @Test
    public void test_callGetApi_should_return_empty_string_when_url_is_invalid() {
        Assert.assertEquals("", Utils.callGetApi(null));
        Assert.assertEquals("", Utils.callGetApi(""));
        Assert.assertEquals("", Utils.callGetApi("abc"));
    }

}
