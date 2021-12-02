package org.saucedemo.extensions;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.saucedemo.factories.EnvFactory;

import static org.saucedemo.extensions.TestExecutionLifecycle.getTestStatus;
import static org.saucedemo.factories.capabilities.localhost.android.EmulatorDevicePicker.freeDevice;

public class TestResult {
    private TestResult(){
        throw new IllegalStateException("Utility class");
    }

    private static final String HOST = EnvFactory.getConfig().getString("HOST").toLowerCase();

    public static void setTestStatus(AppiumDriver driver, String childTestClassName){
        switch (HOST) {
            case "browserstack":
                setTestStatusBrowserStack(driver, childTestClassName);
                break;
            case "saucelabs":
                setTestStatusSauceLabs(driver);
                break;
            case "localhost":
                freeDevice(driver);
                break;
            default:
                break;
        }
    }

    public static void setTestStatusBrowserStack(AppiumDriver driver, String className) {
        JavascriptExecutor jse = (JavascriptExecutor) driver;

        String displayName = className + " : " + TestExecutionLifecycle.getTestName();
        jse.executeScript("browserstack_executor: {\"action\": \"setSessionName\", \"arguments\": {\"name\":\"" + displayName + "\" }}");

        Boolean testStatus = getTestStatus();
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
        if(getTestResult(getTestStatus()).equalsIgnoreCase("passed")){
            driver.executeScript("sauce:job-result=passed");
        } else{
            driver.executeScript("sauce:job-result=failed");
        }
    }
}
