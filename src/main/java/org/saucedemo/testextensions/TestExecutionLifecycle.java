package org.saucedemo.testextensions;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;

import java.lang.reflect.Method;

@Slf4j
public class TestExecutionLifecycle implements BeforeTestExecutionCallback, AfterTestExecutionCallback {
    private static final String TEST_START_TIME = "start time";
    private static String testClassName;
    private static String testName;
    private static Boolean testStatus;
    private static String reason;

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        getStore(context).put(TEST_START_TIME, System.currentTimeMillis());
    }

    private Store getStore(ExtensionContext context) {
        return context.getStore(Namespace.create(getClass(), context.getRequiredTestMethod()));
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        setCurrentTestsVariables(context);
        logCurrentTestsTotalExecutionTime(context);
    }

    private void setCurrentTestsVariables(ExtensionContext context) {
        testName = context.getDisplayName();
        testStatus = context.getExecutionException().isPresent();
        reason = context.getExecutionException().toString();
        testClassName = context.getTestClass().get().getSimpleName();
    }

    private void logCurrentTestsTotalExecutionTime(ExtensionContext context) {
        Method testMethod = context.getRequiredTestMethod();
        long startTime = getStore(context).remove(TEST_START_TIME, long.class);
        double duration = (System.currentTimeMillis() - startTime)/1000.0;

        log.info("Method [{}] took {} seconds.", testMethod.getName(), duration);
    }

    public static String getTestClassName() {
        return testClassName;
    }

    public static String getTestName() {
        return testName;
    }

    public static Boolean getTestStatus() {
        return testStatus;
    }

    public static String getReason() {
        return reason;
    }
}
