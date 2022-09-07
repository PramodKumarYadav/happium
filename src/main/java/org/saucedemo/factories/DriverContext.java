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
public class DriverContext {
    public static final ThreadLocal<AppiumDriver> holder = new ThreadLocal<>();

    private static Config config = EnvFactory.getInstance().getConfig();
    private static final Host HOST = Host.parse(config.getString("HOST"));
    private static final Platform PLATFORM = Platform.parse(config.getString("PLATFORM"));

    public static void setDriver(){
        holder.set(getDriverType());
    }

    public static AppiumDriver getDriver() {
        return holder.get();
    }

    public static void unloadDriver() {
        holder.remove();
    }

    public static AppiumDriver getDriverType() {
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

    public static void setDriverTimeouts(AppiumDriver driver) {
        driver.manage().timeouts().implicitlyWait(180, TimeUnit.SECONDS);

        log.info("SessionId: {}", driver.getSessionId());
        log.info("Driver capabilities: {}", driver.getCapabilities());
    }
}
