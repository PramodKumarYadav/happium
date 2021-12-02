package org.saucedemo.factories;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.typesafe.config.Config;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.saucedemo.enums.Host;
import org.saucedemo.enums.Platform;
import org.saucedemo.factories.capabilities.browserstack.BrowserStackCapabilities;
import org.saucedemo.factories.capabilities.localhost.LocalhostCapabilities;
import org.saucedemo.factories.capabilities.saucelabs.SauceLabsCapabilities;

// https://www.baeldung.com/jackson-object-mapper-tutorial
// Appium Desired Capabilities: https://appium.io/docs/en/writing-running-appium/caps/#appium-desired-capabilities
// Logging of capabilities: https://appiumpro.com/editions/10-anatomy-of-logging-in-appium
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CapabilitiesFactory {
    private CapabilitiesFactory() {
        throw new IllegalStateException("Static factory class");
    }

    /**
     * fetch capabilities for a host and platform. testClassName is just a useful parameter for result logging.
     */
    public static DesiredCapabilities getDesiredCapabilities(Host host, Platform platform, String testClassName) {
        log.info("Running tests for TestClass: {}", testClassName);
        log.info("Running tests on PLATFORM_NAME: {}", platform);

        switch (host) {
            case browserstack:
                return BrowserStackCapabilities.get(testClassName);
            case saucelabs:
                return SauceLabsCapabilities.get(platform, testClassName);
            case localhost:
                return LocalhostCapabilities.get(platform, testClassName);
            default:
                throw new IllegalStateException(String.format("HOST not defined in config file for host: %s", host));
        }
    }
}
