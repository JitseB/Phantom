package net.jitse.phantom.spigot.modules.weather;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class WeatherTypeTest {

    @Test
    public void testLowercase() {
        WeatherType none = WeatherType.byString("sun").orElse(null);
        assertNotNull(none);
        assertSame(none, WeatherType.SUN);
    }

    @Test
    public void testUppercase() {
        WeatherType none = WeatherType.byString("SUN").orElse(null);
        assertNotNull(none);
        assertSame(none, WeatherType.SUN);
    }
}