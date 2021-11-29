package org.saucedemo.factories;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DriverFactory {
    private static final String PLATFORM_NAME = TestEnvironment.getConfig().getString("PLATFORM_NAME").toLowerCase();
    private static final String HOST = TestEnvironment.getConfig().getString("HOST").toLowerCase();

    private DriverFactory() {
        throw new IllegalStateException("Factory class");
    }

    public static AppiumDriver getDriver(String testClassName) {
        switch (PLATFORM_NAME) {
            case "android":
                return new AndroidDriver(getHostURL(), CapabilitiesFactory.getDesiredCapabilities(testClassName));
            case "ios":
                return new IOSDriver(getHostURL(), CapabilitiesFactory.getDesiredCapabilities(testClassName));
            default:
                throw new IllegalStateException(String.format("%s is not a valid platform choice. You can either choose 'android' or 'ios. " +
                        "Check the value of 'PLATFORM_NAME' property in application.conf; Or in CI, if running from continuous integration.", PLATFORM_NAME));
        }
    }

    /**
     * Set implicit wait before using the driver
     */
    public static void setDriver(AppiumDriver driver) {
        driver.manage().timeouts().implicitlyWait(180, TimeUnit.SECONDS);

        log.info("SessionId: {}", driver.getSessionId());
        log.info("Driver capabilities: {}", driver.getCapabilities());
    }

    private static URL getHostURL() {
        try {
            return new URL(getHostUri());
        } catch (MalformedURLException e) {
            throw new IllegalStateException(String.format("%s is Malformed host URL.", getHostUri()), e);
        }
    }

    private static String getHostUri() {
        switch (HOST) {
            case "saucelabs":
                String sauceUri = TestEnvironment.getConfig().getString("SAUCE_URI");
                return "https://" + System.getenv("SAUCE_USERNAME") + ":" + System.getenv("SAUCE_ACCESS_KEY") + sauceUri + "/wd/hub";
            default:
                return TestEnvironment.getConfig().getString("HOST_URI");
        }
    }
}
