package net.jitse.phantom.spigot.modules.weather;

import java.util.Arrays;
import java.util.Optional;

public enum WeatherType {

    SUN, RAIN, STORM;

    public static Optional<WeatherType> byString(String name) {
        return Arrays.stream(values()).filter(type -> type.toString().equalsIgnoreCase(name)).findFirst();
    }
}
