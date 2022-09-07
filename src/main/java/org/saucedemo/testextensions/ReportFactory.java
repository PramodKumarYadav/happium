package org.saucedemo.testextensions;

import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.saucedemo.choices.Host;
import org.saucedemo.factories.DriverFactory;
import org.saucedemo.factories.EnvFactory;

import static org.saucedemo.factories.capabilities.localhost.android.EmulatorDevicePicker.freeDevice;

@Slf4j
public class ReportFactory {
    private static Config config = EnvFactory.getInstance().getConfig();
    private static final Host HOST = Host.parse(config.getString("HOST"));

    private ReportFactory(){
        throw new IllegalStateException("Static factory class");
    }

    public static void publishToHost(TestRunMetaData testRunMetaData){
        switch (HOST) {
            case BROWSERSTACK:
                setTestStatusBrowserStack(testRunMetaData);
                break;
            case SAUCELABS:
                setTestStatusSauceLabs(testRunMetaData);
                break;
            case LOCALHOST:
                freeDevice();
                break;
            default:
                break;
        }
    }

    public static void setTestStatusBrowserStack(TestRunMetaData testRunMetaData) {
        JavascriptExecutor jse = (JavascriptExecutor) DriverFactory.getDriver();

        String displayName = testRunMetaData.getTestClass()  + " : " + testRunMetaData.getTestName();
        jse.executeScript("browserstack_executor: {\"action\": \"setSessionName\", \"arguments\": {\"name\":\"" + displayName + "\" }}");
        jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"" + testRunMetaData.getTestResult() + "\", \"reason\": \"" + testRunMetaData.getReason() + "\"}}");
    }

    public static void setTestStatusSauceLabs(TestRunMetaData testRunMetaData) {
        if(testRunMetaData.getTestResult().equalsIgnoreCase("passed")){
            DriverFactory.getDriver().executeScript("sauce:job-result=passed");
        } else{
            DriverFactory.getDriver().executeScript("sauce:job-result=failed");
        }
    }
}
