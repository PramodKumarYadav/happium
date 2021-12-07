package org.saucedemo.factories.capabilities.browserstack;

import java.util.Random;

// Available android devices: https://www.browserstack.com/list-of-browsers-and-platforms/live
public enum AvailableAndroidModels {
    SAMSUNG("samsung"),
    GOOGLE("google"),
    ONE_PLUS("one-plus"),
    XIAOMI("xiaomi"),
    /**
     * Note: Below commented models are unstable with the current version of app - thus commented. If for your app, they work
     * fine, feel free to uncomment them.
     */
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
