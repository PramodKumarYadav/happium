package org.saucedemo.deviceTypes.browserStack.android;

import java.util.Random;

public enum AndroidModelType {
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

    AndroidModelType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static AndroidModelType getRandomModel() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }
}
