package org.saucedemo.extensions;

import io.appium.java_client.AppiumDriver;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.saucedemo.factories.DriverFactory;
import org.saucedemo.factories.TestEnvironment;

import static org.saucedemo.hosts.localhost.android.EmulatorDevicePicker.freeDevice;

@Slf4j
public class TestSetup {
    public AppiumDriver driver;
    public String childTestClassName;

    @BeforeEach
    public void setUp() {
        this.childTestClassName = this.getClass().getSimpleName();
        this.driver = DriverFactory.getDriver(childTestClassName);
    }

    @AfterEach
    public void tearDown() {
        if(TestEnvironment.getConfig().getString("HOST").equalsIgnoreCase("browserstack")){
            TestResult.setTestStatus(driver, childTestClassName);
        } else {
            freeDevice(driver);
        }

        driver.quit();
        log.info("tear down complete");
    }
}
