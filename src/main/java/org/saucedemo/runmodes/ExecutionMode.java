package org.saucedemo.runmodes;

import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.saucedemo.factories.EnvConfigFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Slf4j
public class ExecutionMode {
    private static final Config config = EnvConfigFactory.getConfig();

    // Since junit execution properties are not going to change mid execution, this method can be final.
    public static final ExecutionModes getExecutionMode() {
        Properties junitProperties = new Properties();

        String pathJunitPlatformProperties = config.getString("pathJunitPlatformProperties");
        junitProperties = getProperties(junitProperties, pathJunitPlatformProperties);

        String parallelMode = junitProperties.getProperty("junit.jupiter.execution.parallel.enabled");
        String testMode = junitProperties.getProperty("junit.jupiter.execution.parallel.mode.default");
        String classMode = junitProperties.getProperty("junit.jupiter.execution.parallel.mode.classes.default");

        log.info("parallelMode: {}", parallelMode);
        log.info("classMode: {}", classMode);
        log.info("testMode: {}", testMode);

        // Run all tests and classes in series
        if (parallelMode.equalsIgnoreCase("false")) {
            log.info("All classes run in series. Within each class, all tests run in series.");
            return ExecutionModes.CLASS_SERIES_TEST_SERIES;
        } else {
            // Run all tests and classes in different parallel modes - except the first option below (which is same as running in series)
            if (classMode.equalsIgnoreCase("same_thread") && testMode.equalsIgnoreCase("same_thread")) {
                log.info("All classes run in series. Within each class, all tests run in series.");
                return ExecutionModes.CLASS_SERIES_TEST_SERIES;
            } else if (classMode.equalsIgnoreCase("same_thread") && testMode.equalsIgnoreCase("concurrent")) {
                log.info("All classes run in Series. Within each class, all tests run in Parallel.");
                return ExecutionModes.CLASS_SERIES_TEST_PARALLEL;
            } else if (classMode.equalsIgnoreCase("concurrent") && testMode.equalsIgnoreCase("same_thread")) {
                log.info("All classes run in Parallel. Within each class, all tests run in series.");
                return ExecutionModes.CLASS_PARALLEL_TEST_SERIES;
            } else if (classMode.equalsIgnoreCase("concurrent") && testMode.equalsIgnoreCase("concurrent")) {
                log.info("All classes run in Parallel. Within each class, all tests run in Parallel.");
                return ExecutionModes.CLASS_PARALLEL_TEST_PARALLEL;
            } else {
                log.error("Invalid mode of execution provided in junit-platform.properties file. Check and correct!");
            }
        }

        log.info("Since no valid choices were not provided; no valid mode can be returned!");
        return null;
    }

    private static Properties getProperties(Properties junitProperties, String pathJunitPlatformProperties) {
        try {
            junitProperties.load(new FileInputStream(pathJunitPlatformProperties));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return junitProperties;
    }
}
