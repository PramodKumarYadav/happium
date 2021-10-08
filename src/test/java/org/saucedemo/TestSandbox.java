package org.saucedemo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.saucedemo.factories.devices.AndroidEmulators;

import static org.saucedemo.factories.CapabilitiesFactory.getDesiredCapabilities;

@Slf4j
public class TestSandbox {
    @Test
    void testDesiredCapabilitiesDefault() {
        DesiredCapabilities capabilities = getDesiredCapabilities();
        log.info(capabilities.toString());
    }

    @Test
    void getDeviceBasedOnLocation() {
        log.info("deviceName @ position 0: {}", AndroidEmulators.values()[0]);
        log.info("deviceName @ position 1: {}", AndroidEmulators.values()[1]);
    }
}
