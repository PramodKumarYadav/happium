package org.saucedemo.extensions;

import io.appium.java_client.*;
import lombok.extern.slf4j.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.saucedemo.factories.DriverFactory;
import org.saucedemo.factories.TestEnvironment;

import static org.saucedemo.hosts.localhost.android.EmulatorDevicePicker.freeDevice;

@Slf4j
public class TestSetup {
    public AppiumDriver driver;
    public String className;

    @BeforeEach
    public void setUp() {
        this.className = this.getClass().getSimpleName();
        driver = DriverFactory.getDriver(className);
    }

    @AfterEach
    public void tearDown() {
        if(TestEnvironment.getConfig().getString("HOST").equalsIgnoreCase("browserstack")){
            TestResult.setTestStatus(driver, className);
        } else {
            freeDevice(driver);
        }

        driver.quit();
        log.info("tear down complete");
    }
}
