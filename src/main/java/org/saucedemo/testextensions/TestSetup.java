package org.saucedemo.testextensions;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.saucedemo.factories.DriverFactory;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TestSetup {
    private static Map<String, String> testThreadMap = new HashMap<>();

    @BeforeEach
    public void setUp() {
        setTestThreadMap();

        DriverFactory.setDriver();
        DriverFactory.setDriverTimeouts();
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
        DriverFactory.removeDriver();
        log.info("tear down complete");
    }
}
