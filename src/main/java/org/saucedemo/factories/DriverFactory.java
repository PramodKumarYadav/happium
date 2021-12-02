package org.saucedemo.factories;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import lombok.extern.slf4j.Slf4j;
import org.saucedemo.enums.Platform;

import java.util.concurrent.TimeUnit;

@Slf4j
public class DriverFactory {
    private static final Platform PLATFORM = Platform.valueOf(TestEnvironment.getConfig().getString("PLATFORM_NAME").toUpperCase());

    private DriverFactory() {
        throw new IllegalStateException("Factory class");
    }

    public static AppiumDriver getDriver(String testClassName) {
        URLFactory urlFactory = new URLFactory();
        switch (PLATFORM) {
            case ANDROID:
                return new AndroidDriver(urlFactory.getHostURL(), CapabilitiesFactory.getDesiredCapabilities(testClassName));
            case IOS:
                return new IOSDriver(urlFactory.getHostURL(), CapabilitiesFactory.getDesiredCapabilities(testClassName));
            default:
                throw new IllegalStateException(String.format("%s is not a valid platform choice. You can either choose 'android' or 'ios. " +
                        "Check the value of 'PLATFORM_NAME' property in application.conf; Or in CI, if running from continuous integration.", PLATFORM));
        }
    }

    public static void setDriverTimeouts(AppiumDriver driver) {
        driver.manage().timeouts().implicitlyWait(180, TimeUnit.SECONDS);

        log.info("SessionId: {}", driver.getSessionId());
        log.info("Driver capabilities: {}", driver.getCapabilities());
    }
}
