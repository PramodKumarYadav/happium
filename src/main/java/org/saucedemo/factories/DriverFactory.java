package org.saucedemo.factories;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import lombok.extern.slf4j.Slf4j;
import org.saucedemo.enums.Host;
import org.saucedemo.enums.Platform;

import java.util.concurrent.TimeUnit;

@Slf4j
public class DriverFactory {
    private static final Host HOST = Host.valueOf(EnvFactory.getConfig().getString("HOST"));
    private static final Platform PLATFORM = Platform.valueOf(EnvFactory.getConfig().getString("PLATFORM_NAME"));

    private DriverFactory() {
        throw new IllegalStateException("Static factory class");
    }

    public static AppiumDriver getDriver(Platform platform , String testClassName) {
        switch (platform) {
            case android:
                return new AndroidDriver(URLFactory.getHostURL(HOST), CapabilitiesFactory.getDesiredCapabilities(HOST, PLATFORM, testClassName));
            case ios:
                return new IOSDriver(URLFactory.getHostURL(HOST), CapabilitiesFactory.getDesiredCapabilities(HOST, PLATFORM, testClassName));
            default:
                throw new IllegalStateException(String.format("%s is not a valid platform choice. Pick your platform from %s." +
                        "Check the value of 'PLATFORM_NAME' property in application.conf; Or in CI, if running from continuous integration.", platform, java.util.Arrays.asList(Platform.values())));
        }
    }

    public static void setDriverTimeouts(AppiumDriver driver) {
        driver.manage().timeouts().implicitlyWait(180, TimeUnit.SECONDS);

        log.info("SessionId: {}", driver.getSessionId());
        log.info("Driver capabilities: {}", driver.getCapabilities());
    }
}
