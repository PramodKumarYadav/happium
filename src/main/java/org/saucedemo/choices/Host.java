package org.saucedemo.choices;

import java.util.HashMap;
import java.util.Map;

public enum Host {
    LOCALHOST("localhost"),
    BROWSERSTACK("browserstack"),
    SAUCELABS("saucelabs");

    public final String label;

    Host(String label) {
        this.label = label;
    }

    private static final Map<String, Host> BY_LABEL = new HashMap<>();

    static {
        for (Host host : values()) {
            BY_LABEL.put(host.label, host);
        }
    }

    // To get enum name from a label (choice specified in application.conf)
    public static Host valueOfLabel(String label) {
        if (BY_LABEL.get(label) == null) {
            throw new IllegalStateException(String.format("%s is not a valid host choice. Pick your host from %s." +
                    "Check the value of 'HOST' property in application.conf; Or in CI, if running from continuous integration.", label, BY_LABEL.keySet()));
        } else {
            return BY_LABEL.get(label);
        }
    }
}
