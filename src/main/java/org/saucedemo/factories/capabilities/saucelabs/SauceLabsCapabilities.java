package org.saucedemo.factories.capabilities.saucelabs;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.saucedemo.enums.Platform;
import org.saucedemo.factories.EnvFactory;

import java.util.Date;

@Slf4j
public class SauceLabsCapabilities {
    private static final Date ADD_DATE_TIME_TO_MAKE_BUILDS_UNIQUE = new Date();

    public static DesiredCapabilities get(String testClassName, Platform platform) {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        // Note that saucelabs user and key are fetched from system env variables. Rest all other properties are fetched from config.
        capabilities.setCapability("username", System.getenv("SAUCE_USERNAME"));
        capabilities.setCapability("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
        capabilities.setCapability("appWaitActivity", "com.swaglabsmobileapp.MainActivity");
        capabilities.setCapability("idleTimeout", "90");
        capabilities.setCapability("noReset", "true");
        capabilities.setCapability("newCommandTimeout", "90");

        // Test result capabilities (build and test class name)
        String buildNameSauce = EnvFactory.getConfig().getString("SAUCE_BUILD_NAME") + " - " + ADD_DATE_TIME_TO_MAKE_BUILDS_UNIQUE;
        capabilities.setCapability("build", buildNameSauce);
        capabilities.setCapability("name", testClassName);

        // todo: add random mode for saucelabs.
//                BrowserStackDevice device = BrowserStackDevicePicker.getDevice();
//                log.info("browserstack device: {} ; {}", device.getDeviceName(), device.getOsVersion());
//                capabilities.setCapability("device", device.getDeviceName());

        capabilities.setCapability("platformName", platform);
        switch (platform) {
            case android:
                capabilities.setCapability("app", "storage:filename=" + EnvFactory.getConfig().getString("ANDROID_APP_NAME"));
//                        OR
//                        capabilities.setCapability("app", CONFIG.getString("ANDROID_APP_URL"));
                capabilities.setCapability("platformVersion", "8.1");
                capabilities.setCapability("deviceName", "Samsung Galaxy S9 Plus FHD GoogleAPI Emulator");
                break;
            case ios:
                break;
            default:
                break;
        }

        log.debug("Capabilities: {}", capabilities);
        return capabilities;
    }
}
