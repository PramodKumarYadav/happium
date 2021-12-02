package org.saucedemo.enums;

public enum Host {
    LOCALHOST("localhost"),
    BROWSERSTACK("browserstack"),
    SAUCELABS("saucelabs");

    private final String value;

    Host(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
