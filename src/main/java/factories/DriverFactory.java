package factories;

import com.typesafe.config.Config;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

import static factories.EnvConfigFactory.getConfig;

@Slf4j
public class DriverFactory {
    private static Config config = getConfig();
    private static String platformName = config.getString("platformName");
    private static String hostURI = config.getString("hostURI");
    private static URL hostURL = getHostURL(hostURI);

    private DriverFactory() {
        // Do not want people to create an instance of Factory but use its static getDriver method to get the driver
        // using private WebDriver driver = DriverFactory.getAuthenticatedDriver();
    }

    // Since you cannot run tests in parallel on the same emulator device.
    // If you want to run your tests in parallel, you must specify different deviceNames for each test from your test class.
    public static AppiumDriver getDriver(String deviceName) {
        AppiumDriver driver = null;
        DesiredCapabilities capabilities = new CapabilitiesFactory().getDesiredCapabilities(deviceName);

        switch (platformName.toLowerCase()) {
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
