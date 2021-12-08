package org.saucedemo.testextensions;

import io.appium.java_client.AppiumDriver;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.saucedemo.choices.Host;

import static org.saucedemo.factories.capabilities.localhost.android.EmulatorDevicePicker.freeDevice;

@Slf4j
public class TestResult {
    private TestResult(){
        throw new IllegalStateException("Utility class");
    }

    public static void setTestStatus(AppiumDriver driver, Host host){
        switch (host) {
            case browserstack:
                setTestStatusBrowserStack(driver);
                break;
            case saucelabs:
                setTestStatusSauceLabs(driver);
                break;
            case localhost:
                freeDevice(driver);
                break;
            default:
                break;
        }
    }

    public static void setTestStatusBrowserStack(AppiumDriver driver) {
        JavascriptExecutor jse = (JavascriptExecutor) driver;

        String displayName = TestExecutionLifecycle.getTestClassName() + " : " + TestExecutionLifecycle.getTestName();
        jse.executeScript("browserstack_executor: {\"action\": \"setSessionName\", \"arguments\": {\"name\":\"" + displayName + "\" }}");

        Boolean testStatus = TestExecutionLifecycle.getTestStatus();
        jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"" + getTestResult(testStatus) + "\", \"reason\": \"" + TestExecutionLifecycle.getReason() + "\"}}");
    }

    public static String getTestResult(Boolean testStatus) {
        if (testStatus == true) {
            return "failed";
        } else {
            return "passed";
        }
    }

    public static void setTestStatusSauceLabs(AppiumDriver driver) {
        if(getTestResult(TestExecutionLifecycle.getTestStatus()).equalsIgnoreCase("passed")){
            driver.executeScript("sauce:job-result=passed");
        } else{
            driver.executeScript("sauce:job-result=failed");
        }
    }
}
