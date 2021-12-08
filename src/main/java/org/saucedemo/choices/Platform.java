package org.saucedemo.choices;

import java.util.HashMap;
import java.util.Map;

public enum Platform {
    ANDROID("android"),
    IOS("ios");

    public final String label;
    Platform(String label) {
        this.label = label;
    }

    private static final Map<String, Platform> BY_LABEL = new HashMap<>();
    static {
        for (Platform platform: values()) {
            BY_LABEL.put(platform.label, platform);
        }
    }

    // To get enum name from a label
    public static Platform valueOfLabel(String label) {
        if(BY_LABEL.get(label) == null){
            throw new IllegalStateException(String.format("%s is not a valid platform choice. Pick your platform from %s." +
                    "Check the value of 'PLATFORM_NAME' property in application.conf; Or in CI, if running from continuous integration.", label, BY_LABEL.keySet()));
        }else{
            return BY_LABEL.get(label);
        }
    }
}
