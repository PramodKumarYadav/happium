package org.saucedemo.factories.hosts.browserstack;

import java.util.Random;

public enum AvailableIOSModels {
    IPHONE("iPhone"),
    IPAD("iPad");

    private final String value;

    AvailableIOSModels(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static AvailableIOSModels getRandomModel() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }
}
