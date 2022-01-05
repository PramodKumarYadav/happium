package org.saucedemo.choices;

import java.util.HashMap;
import java.util.Map;

public enum App {
    LOCAL("local"),
    DEVELOP("develop"),
    STAGING("staging"),
    PRODUCTION("production");

    public final String label;

    App(String label) {
        this.label = label;
    }

    private static final Map<String, App> BY_LABEL = new HashMap<>();

    static {
        for (App app : values()) {
            BY_LABEL.put(app.label, app);
        }
    }

    // To get enum name from a label (choice specified in application.conf)
    public static App parse(String label) {
        if (BY_LABEL.get(label) == null) {
            throw new IllegalStateException(String.format("%s is not a valid app env choice. Pick your app env from %s." +
                    "Check the value of 'APP' property in application.conf; Or in CI, if running from continuous integration.", label, BY_LABEL.keySet()));
        } else {
            return BY_LABEL.get(label);
        }
    }
}
