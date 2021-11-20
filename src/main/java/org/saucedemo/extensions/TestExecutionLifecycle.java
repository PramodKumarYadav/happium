package org.saucedemo.extensions;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;

import java.lang.reflect.*;

@Slf4j
public class TestExecutionLifecycle implements BeforeTestExecutionCallback, AfterTestExecutionCallback {
    private static String testName;
    private static Boolean testStatus;
    private static String className;
    private static String reason;

    private static final String START_TIME = "start time";

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        getStore(context).put(START_TIME, System.currentTimeMillis());
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
        className = context.getTestClass().toString();
        testName = context.getDisplayName();
        testStatus = context.getExecutionException().isPresent();
        reason = context.getExecutionException().toString();
    }

    private void logCurrentTestsTotalExecutionTime(ExtensionContext context) {
        Method testMethod = context.getRequiredTestMethod();
        long startTime = getStore(context).remove(START_TIME, long.class);
        double duration = (System.currentTimeMillis() - startTime)/1000.0;

        log.info("Method [{}] took {} seconds.", testMethod.getName(), duration);
    }

    public static String getTestName() {
        return testName;
    }

    public static Boolean getTestStatus() {
        return testStatus;
    }

    public static String getClassName() {
        return className;
    }

    public static String getReason() {
        return reason;
    }
}
