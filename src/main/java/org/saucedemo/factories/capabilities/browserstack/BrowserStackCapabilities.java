package org.saucedemo.factories.capabilities.browserstack;

import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.saucedemo.enums.Platform;
import org.saucedemo.extensions.TestSetup;
import org.saucedemo.factories.EnvFactory;

import java.util.Date;

@Slf4j
public class BrowserStackCapabilities {
    private static Config config = EnvFactory.getInstance().getConfig();
    private static final Date ADD_DATE_TIME_TO_MAKE_BUILDS_UNIQUE = new Date();
    private static final Platform PLATFORM = Platform.valueOf(config.getString("PLATFORM_NAME"));

    public BrowserStackCapabilities () {
    }

    public DesiredCapabilities get() {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        // Note that browserstack user and key are fetched from system env variables. Rest all other properties are fetched from config.
        // Github does not allow dots in secrets. So I have to store these keys as underscores (i.e. different than browserstack specifies it to be.
        capabilities.setCapability("browserstack.user", System.getenv("BROWSERSTACK_USER"));
        capabilities.setCapability("browserstack.key", System.getenv("BROWSERSTACK_KEY"));

        capabilities.setCapability("name", TestSetup.getTestThreadMap().get(Thread.currentThread().getName()));
        capabilities.setCapability("browserstack.networkLogs", true);

        setAppCapability(PLATFORM, capabilities);
        setReportingCapabilities(capabilities);
        setDeviceCapabilities(capabilities);

        log.debug("Capabilities: {}", capabilities);
        return capabilities;
    }

    private static void setAppCapability(Platform platform, DesiredCapabilities capabilities) {
        log.info("Setting right app for platform: {}", platform);
        switch (platform) {
            case android:
                capabilities.setCapability("app", System.getenv("BROWSERSTACK_USER") + "/" + config.getString("CUSTOM_ID_ANDROID"));
                break;
            case ios:
                capabilities.setCapability("app", System.getenv("BROWSERSTACK_USER") + "/" + config.getString("CUSTOM_ID_IOS_REAL_DEVICE"));
                break;
            default:
                break;
        }
    }

    private static void setReportingCapabilities(DesiredCapabilities capabilities) {
        capabilities.setCapability("project", config.getString("PROJECT"));
        String buildName = String.format("from %s - on %s - at %s",config.getString("BROWSERSTACK_BUILD_NAME"), PLATFORM, ADD_DATE_TIME_TO_MAKE_BUILDS_UNIQUE);
        capabilities.setCapability("build", buildName);
        log.info("buildName: {}", buildName);
    }

    private void setDeviceCapabilities(DesiredCapabilities capabilities) {
        BrowserStackDevice device = new BrowserStackDeviceFactory().getDevice();
        capabilities.setCapability("device", device.getDeviceName());
        capabilities.setCapability("os_version", device.getOsVersion());
        log.info("browserstack device: {} ; {}", device.getDeviceName(), device.getOsVersion());
    }
}
