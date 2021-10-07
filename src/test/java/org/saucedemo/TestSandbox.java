package org.saucedemo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.saucedemo.factories.DeviceList;

import static org.saucedemo.factories.CapabilitiesFactory.getDesiredCapabilities;

@Slf4j
public class TestSandbox {
    private static String deviceName = "Pixel_4a_API_31";

    @Test
    void testDesiredCapabilitiesDefault() {
        DesiredCapabilities capabilities = getDesiredCapabilities();
        log.info(capabilities.toString());
    }

    @Test
    void testDesiredCapabilities() {
        DesiredCapabilities capabilities = getDesiredCapabilities(deviceName);
        log.info(capabilities.toString());
    }

    @Test
    void getDeviceBasedOnLocation() {
        log.info("deviceName @ position 0: {}", DeviceList.values()[0]);
        log.info("deviceName @ position 1: {}", DeviceList.values()[1]);
    }
}
