package org.saucedemo.testresults;

import com.typesafe.config.Config;
import io.appium.java_client.AppiumDriver;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.saucedemo.factories.EnvConfigFactory;

import static org.saucedemo.hosts.localhost.android.EmulatorDevicePicker.freeDevice;
import static org.saucedemo.testresults.RunnerExtension.getClassName;
import static org.saucedemo.testresults.RunnerExtension.getReason;
import static org.saucedemo.testresults.RunnerExtension.getTestName;
import static org.saucedemo.testresults.RunnerExtension.getTestStatus;

@Slf4j
public class TestResult {
    private static final Config CONFIG = EnvConfigFactory.getConfig();

    public static void setTestStatus(AppiumDriver driver) {
        String className = getClassName();
        String testName = getTestName();
        Boolean testStatus = getTestStatus();
        String reason = getReason();

        log.info("className: {}", className);
        log.info("testName: {}", testName);
        log.info("testStatus: {}", testStatus);
        log.info("reason: {}", reason);

        String displayName = className + " : " + testName;
        String testResult = getTestResult(testStatus);

        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("browserstack_executor: {\"action\": \"setSessionName\", \"arguments\": {\"name\":\""+ displayName + "\" }}");
        jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \""+ testResult +"\", \"reason\": \"" + reason + "\"}}");
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

    public static void packUp(AppiumDriver driver) {
        if(CONFIG.getString("HOST").equalsIgnoreCase("browserstack")){
            setTestStatus(driver);
        } else {
            freeDevice(driver);
        }
        
        driver.quit();
        log.info("tear down complete");
    }
}
