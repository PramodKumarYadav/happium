package org.saucedemo.testextensions;

import com.typesafe.config.Config;
import io.appium.java_client.AppiumDriver;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.saucedemo.choices.Host;
import org.saucedemo.choices.Platform;
import org.saucedemo.factories.DriverFactory;
import org.saucedemo.factories.EnvFactory;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TestSetup {
    private static Config config = EnvFactory.getInstance().getConfig();
    private static final Platform PLATFORM = Platform.valueOf(config.getString("PLATFORM_NAME"));
    private static final Host HOST = Host.valueOf(config.getString("HOST"));

    public AppiumDriver driver;
    private static Map<String, String> testThreadMap = new HashMap<>();

    @BeforeEach
    public void setUp() {
        setTestThreadMap();

        this.driver = DriverFactory.getDriver(PLATFORM);
        DriverFactory.setDriverTimeouts(driver);
    }

    private void setTestThreadMap() {
        String testClassName = this.getClass().getSimpleName();
        log.info("testClassName: {}", testClassName);
        testThreadMap.put(Thread.currentThread().getName(), testClassName);
    }

    public static Map<String, String> getTestThreadMap() {
        return testThreadMap;
    }

    @AfterEach
    public void tearDown() {
        TestResult.setTestStatus(driver, HOST);

        driver.quit();
        log.info("tear down complete");
    }
}
