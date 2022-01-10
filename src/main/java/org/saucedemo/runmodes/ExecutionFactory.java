package org.saucedemo.runmodes;

import lombok.extern.slf4j.Slf4j;
import org.saucedemo.factories.EnvFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * For any particular run, the values in junit-platform.properties file will remain constant for all classes using it.
 * Thus we will follow Singleton design pattern here.
 */
@Slf4j
public class ExecutionFactory {
    private static final String SAME_THREAD = "same_thread";
    private static final String CONCURRENT = "concurrent";
    /**
     * With this approach, we are relying on JVM to create the unique instance of ExecutionFactory when the class is loaded.
     * The JVM guarantees that the instance will be created before any thread accesses the static uniqueInstance variable.
     * This code is thus guaranteed to be thread safe.
     */
    private static ExecutionFactory uniqueInstance = new ExecutionFactory();

    private ExecutionFactory() {
        /** Once we have created an instance here, we do not allow other calling classes to make another instance.
         * So all tests can make use of the uniqueInstance available above and call below methods on it.
         */
    }

    public static ExecutionFactory getInstance() {
        return uniqueInstance;
    }

    // Since junit execution properties are not going to change mid execution, this method can be final.
    public ExecutionMode getExecutionMode() {
        Properties junitProperties = getProperties();

        String parallelMode = junitProperties.getProperty("junit.jupiter.execution.parallel.enabled");
        log.info("parallelMode: {}", parallelMode);

        String classMode = junitProperties.getProperty("junit.jupiter.execution.parallel.mode.classes.default");
        log.info("classMode: {}", classMode);

        String testMode = junitProperties.getProperty("junit.jupiter.execution.parallel.mode.default");
        log.info("testMode: {}", testMode);

        // Run all tests and classes in series
        if (parallelMode.equalsIgnoreCase("false")) {
            return ExecutionMode.CLASS_SERIES_TEST_SERIES;
        } else {
            // Run all tests and classes in different parallel modes - except the first option below (which is same as running in series)
            if (classMode.equalsIgnoreCase(SAME_THREAD) && testMode.equalsIgnoreCase(SAME_THREAD)) {
                return ExecutionMode.CLASS_SERIES_TEST_SERIES;
            } else if (classMode.equalsIgnoreCase(SAME_THREAD) && testMode.equalsIgnoreCase(CONCURRENT)) {
                return ExecutionMode.CLASS_SERIES_TEST_PARALLEL;
            } else if (classMode.equalsIgnoreCase(CONCURRENT) && testMode.equalsIgnoreCase(SAME_THREAD)) {
                return ExecutionMode.CLASS_PARALLEL_TEST_SERIES;
            } else if (classMode.equalsIgnoreCase(CONCURRENT) && testMode.equalsIgnoreCase(CONCURRENT)) {
                return ExecutionMode.CLASS_PARALLEL_TEST_PARALLEL;
            } else {
                throw new IllegalStateException("Invalid mode of execution provided in junit-platform.properties file. Check and correct!");
            }
        }
    }

    public String getConfigStrategy() {
        String configStrategy = getProperties().getProperty("junit.jupiter.execution.parallel.config.strategy");
        log.info("configStrategy: {}", configStrategy);

        return configStrategy;
    }

    public Integer getFixedThreadCount() {
        String fixedThreadCount = getProperties().getProperty("junit.jupiter.execution.parallel.config.fixed.parallelism");
        log.info("fixedThreadCount: {}", fixedThreadCount);

        return Integer.parseInt(fixedThreadCount);
    }

    private Properties getProperties() {
        Properties junitProperties = new Properties();
        try {
            junitProperties.load(new FileInputStream(EnvFactory.getInstance().getConfig().getString("PATH_JUNIT_PLATFORM_PROPERTIES")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return junitProperties;
    }
}
