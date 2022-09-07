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
    private static Config config = EnvFactory.getInstance().getConfig();
    private static final Host HOST = Host.parse(config.getString("HOST"));

    private DriverFactory() {
        throw new IllegalStateException("Static factory class");
    }

    public static AppiumDriver getDriver(Platform platform) {
        setDriverContext(platform);
        return DriverContext.holder.get();
    }

    private static void setDriverContext(Platform platform) {
        log.info("Getting driver for PLATFORM: {}", platform);
        switch (platform) {
            case ANDROID:
                DriverContext.holder.set( new AndroidDriver(URLFactory.getHostURL(HOST), CapabilitiesFactory.getDesiredCapabilities(HOST, platform)));
                break;
            case IOS:
                DriverContext.holder.set(new IOSDriver(URLFactory.getHostURL(HOST), CapabilitiesFactory.getDesiredCapabilities(HOST, platform)));
                break;
            default:
                throw new IllegalStateException(String.format("%s is not a valid platform choice. Pick your platform from %s.", platform, java.util.Arrays.asList(Platform.values())));
        }
    }

    public static void setDriverTimeouts(AppiumDriver driver) {
        driver.manage().timeouts().implicitlyWait(180, TimeUnit.SECONDS);

        log.info("SessionId: {}", driver.getSessionId());
        log.info("Driver capabilities: {}", driver.getCapabilities());
    }
}
