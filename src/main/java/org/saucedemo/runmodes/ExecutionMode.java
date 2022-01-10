package org.saucedemo.runmodes;

import java.util.HashMap;
import java.util.Map;

public enum RunMode {
    CLASS_SERIES_TEST_SERIES("All classes run in series. Within each class, all tests run in series."),
    CLASS_SERIES_TEST_PARALLEL("All classes run in Series. Within each class, all tests run in Parallel."),
    CLASS_PARALLEL_TEST_SERIES("All classes run in Parallel. Within each class, all tests run in series."),
    CLASS_PARALLEL_TEST_PARALLEL("All classes run in Parallel. Within each class, all tests run in Parallel.");

    public final String label;

    RunMode(String label) {
        this.label = label;
    }

    private static final Map<String, RunMode> BY_LABEL = new HashMap<>();

    static {
        for (RunMode runMode : values()) {
            BY_LABEL.put(runMode.label, runMode);
        }
    }

    // To get enum name from a label (choice specified in application.conf)
    public static RunMode parse(String label) {
        if (BY_LABEL.get(label) == null) {
            throw new IllegalStateException(String.format("%s is not a valid app env choice. Pick your app env from %s." +
                    "Check the value of 'APP' property in application.conf; Or in CI, if running from continuous integration.", label, BY_LABEL.keySet()));
        } else {
            return BY_LABEL.get(label);
        }
    }
}
