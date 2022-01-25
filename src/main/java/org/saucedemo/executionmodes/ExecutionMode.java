package org.saucedemo.executionmodes;

public enum ExecutionMode {
    CLASS_SERIES_TEST_SERIES("All classes run in series. Within each class, all tests run in series."),
    CLASS_SERIES_TEST_PARALLEL("All classes run in Series. Within each class, all tests run in Parallel."),
    CLASS_PARALLEL_TEST_SERIES("All classes run in Parallel. Within each class, all tests run in series."),
    CLASS_PARALLEL_TEST_PARALLEL("All classes run in Parallel. Within each class, all tests run in Parallel.");

    public final String label;

    ExecutionMode(String label) {
        this.label = label;
    }
}
