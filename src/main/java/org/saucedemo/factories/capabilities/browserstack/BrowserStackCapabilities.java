package org.saucedemo.factories.capabilities.browserstack;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.saucedemo.factories.EnvFactory;

import java.util.Date;

@Slf4j
public class BrowserStackCapabilities {
    private static final Date ADD_DATE_TIME_TO_MAKE_BUILDS_UNIQUE = new Date();

    public static DesiredCapabilities get(String testClassName) {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        // Note that browserstack user and key are fetched from system env variables. Rest all other properties are fetched from config.
        // Github does not allow dots in secrets. So I have to store these keys as underscores (i.e. different than browserstack specifies it to be.
        capabilities.setCapability("browserstack.user", System.getenv("BROWSERSTACK_USER"));
        capabilities.setCapability("browserstack.key", System.getenv("BROWSERSTACK_KEY"));
        capabilities.setCapability("app", System.getenv("BROWSERSTACK_USER") + "/" + EnvFactory.getConfig().getString("CUSTOM_ID"));

        capabilities.setCapability("project", EnvFactory.getConfig().getString("PROJECT"));
        String buildName = EnvFactory.getConfig().getString("BROWSERSTACK_BUILD_NAME") + " - " + ADD_DATE_TIME_TO_MAKE_BUILDS_UNIQUE;
        capabilities.setCapability("build", buildName);
        log.info("buildName: {}", buildName);

        capabilities.setCapability("name", testClassName);
        capabilities.setCapability("browserstack.networkLogs", true);

        BrowserStackDevice device = BrowserStackDevicePicker.getDevice();
        log.info("browserstack device: {} ; {}", device.getDeviceName(), device.getOsVersion());
        capabilities.setCapability("device", device.getDeviceName());
        capabilities.setCapability("os_version", device.getOsVersion());

        log.debug("Capabilities: {}", capabilities);
        return capabilities;
    }
}
