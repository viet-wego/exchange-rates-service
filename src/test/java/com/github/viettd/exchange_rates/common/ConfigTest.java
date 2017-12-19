package com.github.viettd.exchange_rates.common;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Config.class)
public class ConfigTest {

    @Test
    public void test_getConfig_should_return_empty_string_when_configuration_is_not_initialized() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Config.class.getDeclaredMethod("getConfig", String.class, String.class);
        method.setAccessible(true);
        Assert.assertEquals("", method.invoke(null, "", ""));
    }

    @Test
    public void test_getIntConfig_should_return_zero_when_configuration_is_not_initialized() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Config.class.getDeclaredMethod("getIntConfig", String.class, String.class);
        method.setAccessible(true);
        Assert.assertEquals(0, method.invoke(null, "", ""));
    }

}
