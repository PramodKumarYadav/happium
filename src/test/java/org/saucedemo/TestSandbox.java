package org.saucedemo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.saucedemo.factories.capabilities.CapabilitiesFactory;

@Slf4j
public class TestSandbox {
    private static String deviceName = "Pixel_XL_API_31";

    @Test
    void testDesiredCapabilitiesDefault() {
        DesiredCapabilities capabilities = new CapabilitiesFactory().getDesiredCapabilities();
        log.info(capabilities.toString());
    }

    @Test
    void testDesiredCapabilities() {
        DesiredCapabilities capabilities = new CapabilitiesFactory().getDesiredCapabilities(deviceName);
        log.info(capabilities.toString());
    }
}
