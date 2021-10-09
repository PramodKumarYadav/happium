package org.saucedemo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.saucedemo.factories.devices.AndroidEmulators;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    void getJunitProperties() throws IOException {
        Properties junitProperties = new Properties();
        String appConfigPath = "D:\\happium\\src\\main\\resources\\junit-platform.properties";
        junitProperties.load(new FileInputStream(appConfigPath));

        String parallelMode = junitProperties.getProperty("junit.jupiter.execution.parallel.enabled");
        String testMode = junitProperties.getProperty("junit.jupiter.execution.parallel.mode.default");
        String classMode = junitProperties.getProperty("junit.jupiter.execution.parallel.mode.classes.default");

        assertAll("Product Details"
                , () -> assertEquals("true", parallelMode)
                , () -> assertEquals("concurrent", testMode)
                , () -> assertEquals("concurrent", classMode)
        );
    }
}
