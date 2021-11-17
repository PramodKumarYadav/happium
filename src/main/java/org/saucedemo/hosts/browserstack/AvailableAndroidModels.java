package org.saucedemo.hosts.browserstack;

import java.util.Random;

public enum AvailableAndroidModels {
    SAMSUNG("samsung"),
    GOOGLE("google"),
    ONE_PLUS("one-plus"),
    XIAOMI("xiaomi"),
//    VIVO("vivo"),
//    HUAWEI("huawei"),
//    OPPO("oppo"),
    MOTOROLA("motorola"),
    ;

    private final String value;

    AvailableAndroidModels(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static AvailableAndroidModels getRandomModel() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }
}
