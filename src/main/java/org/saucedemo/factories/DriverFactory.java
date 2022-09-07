package org.saucedemo.factories;

import com.typesafe.config.Config;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import lombok.extern.slf4j.Slf4j;
import org.saucedemo.choices.Host;
import org.saucedemo.choices.Platform;

import java.util.concurrent.TimeUnit;

@Slf4j
public class DriverFactory {
    /**
     * By using the ThreadLocal, now any page or utility class that wants to use this driver instance can directly
     * access it from this class. We do not have to pass the driver around from one class to another anymore (which is
     * what we had to do when we wanted to use the same driver across page and utility classes. This simplifies the
     * design immensely. In absence of this, you have to initialise the driver in a TestSetup class and toss it around
     * initializing all page objects and utility classes.
     */
    private static final ThreadLocal<AppiumDriver> holder = new ThreadLocal<>();

    private static Config config = EnvFactory.getInstance().getConfig();
    private static final Host HOST = Host.parse(config.getString("HOST"));
    private static final Platform PLATFORM = Platform.parse(config.getString("PLATFORM"));

    public static void setDriver() {
        holder.set(getDriverInstance());
    }

    public static AppiumDriver getDriver() {
        return holder.get();
    }

    public static void removeDriver() {
        holder.remove();
    }

    private static AppiumDriver getDriverInstance() {
        log.info("Getting driver for PLATFORM: {}", PLATFORM);
        switch (PLATFORM) {
            case ANDROID:
                return new AndroidDriver(URLFactory.getHostURL(HOST), CapabilitiesFactory.getDesiredCapabilities(HOST, PLATFORM));
            case IOS:
                return new IOSDriver(URLFactory.getHostURL(HOST), CapabilitiesFactory.getDesiredCapabilities(HOST, PLATFORM));
            default:
                throw new IllegalStateException(String.format("%s is not a valid platform choice. Pick your platform from %s.", PLATFORM, java.util.Arrays.asList(Platform.values())));
        }
    }

    public static void setDriverTimeouts() {
        getDriver().manage().timeouts().implicitlyWait(180, TimeUnit.SECONDS);

        log.info("SessionId: {}", getDriver().getSessionId());
        log.info("Driver capabilities: {}", getDriver().getCapabilities());
    }
}
