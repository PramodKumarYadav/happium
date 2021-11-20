package org.saucedemo.extensions;

import io.appium.java_client.AppiumDriver;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;

import static org.saucedemo.extensions.TestExecutionLifecycle.getTestStatus;

@Slf4j
public class TestResult {
    public static void setTestStatus(AppiumDriver driver, String className) {
        JavascriptExecutor jse = (JavascriptExecutor) driver;

        String displayName = className + " : " + TestExecutionLifecycle.getTestName();
        jse.executeScript("browserstack_executor: {\"action\": \"setSessionName\", \"arguments\": {\"name\":\""+ displayName + "\" }}");

        Boolean testStatus = getTestStatus();
        jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \""+ getTestResult(testStatus) +"\", \"reason\": \"" + TestExecutionLifecycle.getReason() + "\"}}");
    }

    public static String getTestResult(Boolean testStatus){
        String testResult;
        if(testStatus == true){
            testResult = "failed";
        }else{
            testResult = "passed";
        }

        return testResult;
    }
}
