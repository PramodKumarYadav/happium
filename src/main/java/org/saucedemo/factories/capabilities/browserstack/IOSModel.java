package org.saucedemo.factories.capabilities.browserstack;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

// Available ios devices: https://www.browserstack.com/list-of-browsers-and-platforms/live
public enum IOSModel {
    IPHONE("iPhone")
    /**
     * Reason for commenting out IPAD option: Our test app used in this project is only build for iphone.
     * But if you real app is also supported on ipad then uncomment the below line to be used in fixed or random modes.
     * Actual run log: [BROWSERSTACK_APP_BUILT_FOR_IPHONE] The device specified in the 'device' capability is ipad pro 11 2021.
     * However, the app was built for iPhone. Please use an app built for iPad or specify an iPhone in 'device' capability
    */
//    , IPAD("iPad")
    ;

    public final String label;
    IOSModel(String label) {
        this.label = label;
    }

    private static final Map<String, IOSModel> BY_LABEL = new HashMap<>();
    static {
        for (IOSModel IOSModel : values()) {
            BY_LABEL.put(IOSModel.label, IOSModel);
        }
    }

    // To get enum name from a label
    public static IOSModel valueOfLabel(String label) {
        if(BY_LABEL.get(label) == null){
            throw new IllegalStateException(String.format("%s is not a valid ios model. Pick your device from %s." +
                    "Check the value of 'DEVICE' property in browserstack.conf file; Or in CI -> if running from continuous integration.", label, BY_LABEL.keySet()));
        }else{
            return BY_LABEL.get(label);
        }
    }

    public static IOSModel getRandomModel() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }
}
