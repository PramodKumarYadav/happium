package org.saucedemo.factories;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.saucedemo.choices.Host;
import org.saucedemo.choices.Platform;

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
    public static DesiredCapabilities getDesiredCapabilities(Host host, Platform platform) {
        log.info("Getting DesiredCapabilities for Host: {}", host);
        switch (host) {
            case BROWSERSTACK:
                return new BrowserStackCapabilities().get(platform);
            case SAUCELABS:
                return SauceLabsCapabilities.get(platform);
            case LOCALHOST:
                return LocalhostCapabilities.get(platform);
            default:
                throw new IllegalStateException(String.format("HOST not defined in config file for host: %s", host));
        }
    }
}
