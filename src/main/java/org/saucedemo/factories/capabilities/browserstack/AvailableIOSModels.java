package org.saucedemo.factories.capabilities.browserstack;

import java.util.Random;

// Available ios devices: https://www.browserstack.com/list-of-browsers-and-platforms/live
public enum AvailableIOSModels {
    IPHONE("iPhone")
    /**
     * Reason for commenting out IPAD option: Our test app used in this project is only build for iphone.
     * But if you real app is also supported on ipad then uncomment the below line to be used in fixed or random modes.
     * Actual run log: [BROWSERSTACK_APP_BUILT_FOR_IPHONE] The device specified in the 'device' capability is ipad pro 11 2021.
     * However, the app was built for iPhone. Please use an app built for iPad or specify an iPhone in 'device' capability
    */
//    , IPAD("iPad")
    ;

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
