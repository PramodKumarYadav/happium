package org.saucedemo.factories;

import com.typesafe.config.Config;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.saucedemo.factories.CapabilitiesFactory.getDesiredCapabilities;

@Slf4j
public class DriverFactory {
    // Don't want to create any driver for this factory class.
    private DriverFactory() {
    }

    /** Note: When you are running your tests in CI, you can have two CI jobs: One, for platform android and another for iOS- Or randomize it when the time comes.
     Note that when you will overwrite the PLATFORM_NAME value from "mvn clean test command", it will automatically be picked in EnvConfigFactory.getConfig().
     So you would not need to change anything anywhere in any of the tests.
    */
    public static AppiumDriver getDriver(String testClassName) {
        Config CONFIG = EnvConfigFactory.getConfig();
        String PLATFORM_NAME = CONFIG.getString("PLATFORM_NAME");
        String HOST_URI = CONFIG.getString("HOST_URI");

        AppiumDriver driver;
        switch (PLATFORM_NAME) {
            case "android":
                driver = new AndroidDriver(getHostURL(HOST_URI), getDesiredCapabilities(testClassName));
                break;
            case "ios":
                driver = new IOSDriver(getHostURL(HOST_URI), getDesiredCapabilities(testClassName));
                break;
            default:
                throw new IllegalStateException(String.format("%s is not a valid platform choice. You can either choose 'android' or 'ios. " +
                        "Check the value of 'PLATFORM_NAME' property in application.conf; Or in CI, if running from continuous integration.", PLATFORM_NAME));
        }

        log.info("SessionId: {}", driver.getSessionId());
        driver.manage().timeouts().implicitlyWait(180, TimeUnit.SECONDS);

        log.info("Driver capabilities: {}", driver.getCapabilities());
        return driver;
    }

    private static URL getHostURL(String URL) {
        try {
            return new URL(URL);
        } catch (MalformedURLException e) {
            throw new IllegalStateException(String.format("%s is Malformed host URL.", URL), e);
        }
    }
}
