package org.saucedemo.extensions;

import io.appium.java_client.AppiumDriver;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.saucedemo.enums.Platform;
import org.saucedemo.factories.DriverFactory;
import org.saucedemo.factories.EnvFactory;

@Slf4j
public class TestSetup {
    private static final Platform PLATFORM = Platform.valueOf(EnvFactory.getConfig().getString("PLATFORM_NAME"));

    public AppiumDriver driver;
    public String testClassName;

    @BeforeEach
    public void setUp() {
        this.testClassName = this.getClass().getSimpleName();
        this.driver = DriverFactory.getDriver(PLATFORM, testClassName);
        DriverFactory.setDriverTimeouts(driver);
    }

    @AfterEach
    public void tearDown() {
        TestResult.setTestStatus(driver, testClassName);

        driver.quit();
        log.info("tear down complete");
    }
}
