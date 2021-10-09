package org.saucedemo.factories;

import com.typesafe.config.Config;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.saucedemo.factories.CapabilitiesFactory.getDesiredCapabilities;

@Slf4j
public class DriverFactory {
    private static Config config = EnvConfigFactory.getConfig();
    private static String platformName = config.getString("platformName").toLowerCase();
    private static String hostURI = config.getString("hostURI");
    private static URL hostURL = getHostURL(hostURI);

    // Don't want to create any driver for this factory class.
    private DriverFactory() {
    }

    /* Note1: Below method should be your preferred method to call driver.
     Since at any given moment you are either on Android machine OR on a mac machine. Not both. Thus accordingly
     You can specify your choice in application.conf file.

     Note2: When you are running your tests in CI, you can have two CI jobs: One, for platform android and another for iOS. Or randomize it when the time comes.
     Note that when you will overwrite the platformName value from "mvn clean test command", it will automatically be picked by this below method.
     So you would not need to change anything anywhere in any of the tests.
    */

    public static AppiumDriver getDriver() {
        return getDriver(platformName);
    }

    public static AppiumDriver getDriver(String platformName) {
        AppiumDriver driver = null;

        // The device to be chosen, and thus its capabilities, is done in CapabilitiesFactory and is not drivers concern.
        DesiredCapabilities capabilities = getDesiredCapabilities();

        switch (platformName) {
            case "android":
                driver = new AndroidDriver(hostURL, capabilities);
                break;
            case "ios":
                driver = new IOSDriver(hostURL, capabilities);
                break;
            default:
                log.error("Platform choice is incorrect. You can either choose 'android' or 'ios'.");
                log.error("Check the value of 'platformName' property set in application.conf. Or in CI, if run from continuous integration.");
                break;
        }

        log.info("SessionId: {}", driver.getSessionId());
        driver.manage().timeouts().implicitlyWait(180, TimeUnit.SECONDS);

        log.info("Driver capabilities: {}", driver.getCapabilities());
        return driver;
    }

    private static URL getHostURL(String URL) {
        URL url = null;
        try {
            url = new URL(URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
}
