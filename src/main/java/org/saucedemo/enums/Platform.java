package org.saucedemo.enums;

public enum Platform {
    ANDROID("android"),
    IOS("ios");

    private final String value;

    Platform(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
