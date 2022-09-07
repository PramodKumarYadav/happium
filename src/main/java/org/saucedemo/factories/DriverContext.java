package org.saucedemo.factories;

import io.appium.java_client.AppiumDriver;

public class DriverContext {
    public static ThreadLocal<AppiumDriver> holder = new ThreadLocal<>();
}
