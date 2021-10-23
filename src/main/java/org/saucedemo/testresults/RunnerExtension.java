package org.saucedemo.testresults;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

@Slf4j
public class RunnerExtension implements AfterTestExecutionCallback {
    private static String testName;
    private static Boolean testStatus;
    private static String className;
    private static String reason;

    @Override
    public void afterTestExecution(ExtensionContext context) {
        className = context.getTestClass().toString();
        testName = context.getDisplayName();
        testStatus = context.getExecutionException().isPresent();
        reason = context.getExecutionException().toString();

        System.out.println("Finished afterTestExecution");
    }

    public static String getTestName(){
        return testName;
    }

    public static Boolean getTestStatus(){
        return testStatus;
    }

    public static String getClassName(){
        return className;
    }

    public static String getReason(){
        return reason;
    }
}
