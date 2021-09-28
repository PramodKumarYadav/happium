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

@Slf4j
public class DriverFactory {
    private static Config config = EnvConfigFactory.getConfig();
    private static String platformName = config.getString("platformName").toLowerCase();
    private static String hostURI = config.getString("hostURI");
    private static URL hostURL = getHostURL(hostURI);

    private DriverFactory() {
        // Do not want people to create an instance of Factory but use its static getDriver method to get the driver
        // using private WebDriver driver = DriverFactory.getAuthenticatedDriver();
    }

    // Non preferred method. Since tests need not have to care about the device. That should be concern of capabilities.
    public static AppiumDriver getDriver(String deviceName) {
        return getDriver(deviceName);
    }

    // This should be your preferred method to call driver. Which device to pick from available devices is a responsibility
    // we can delegate to capabilities class.
    public static AppiumDriver getDriver() {
        AppiumDriver driver = null;
        DesiredCapabilities capabilities = new CapabilitiesFactory().getDesiredCapabilities();

        switch (platformName) {
            case "android":
                driver = new AndroidDriver(hostURL, capabilities);
                break;
            case "ios":
                driver =  new IOSDriver(hostURL, capabilities);
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
