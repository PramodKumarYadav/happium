package org.saucedemo.extensions;

import io.appium.java_client.AppiumDriver;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.saucedemo.enums.Platform;
import org.saucedemo.factories.DriverFactory;
import org.saucedemo.factories.EnvFactory;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TestSetup {
    private static final Platform PLATFORM = Platform.valueOf(EnvFactory.getConfig().getString("PLATFORM_NAME"));

    public AppiumDriver driver;
    public String testClassName;

    private static Map<String, String> testThreadMap = new HashMap<>();

    @BeforeEach
    public void setUp() {
        this.testClassName = this.getClass().getSimpleName();
        log.info("testClassName: {}", testClassName);
        testThreadMap.put(Thread.currentThread().getName(), this.testClassName);

        this.driver = DriverFactory.getDriver(PLATFORM);
        DriverFactory.setDriverTimeouts(driver);
    }

    @AfterEach
    public void tearDown() {
        TestResult.setTestStatus(driver);

        driver.quit();
        log.info("tear down complete");
    }

    public static Map<String, String> getTestThreadMap(){
        return testThreadMap;
    }
}
