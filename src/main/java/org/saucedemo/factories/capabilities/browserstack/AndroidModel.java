package org.saucedemo.factories.capabilities.browserstack;

import org.saucedemo.choices.Platform;

import java.util.HashMap;
import java.util.Map;
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

    public final String label;
    AvailableAndroidModels(String label) {
        this.label = label;
    }

    private static final Map<String, AvailableAndroidModels> BY_LABEL = new HashMap<>();
    static {
        for (AvailableAndroidModels availableAndroidModels: values()) {
            BY_LABEL.put(availableAndroidModels.label, availableAndroidModels);
        }
    }

    // To get enum name from a label
    public static AvailableAndroidModels valueOfLabel(String label) {
        if(BY_LABEL.get(label) == null){
            throw new IllegalStateException(String.format("%s is not a valid android model. Pick your device from %s." +
                    "Check the value of 'DEVICE' property in browserstack.conf file; Or in CI -> if running from continuous integration.", label, BY_LABEL.keySet()));
        }else{
            return BY_LABEL.get(label);
        }
    }

    public static AvailableAndroidModels getRandomModel() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }
}
