package org.saucedemo.factories;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.typesafe.config.Config;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Iterator;

import static org.saucedemo.factories.AvailableDevices.getDevice;
import static org.saucedemo.utils.FileUtils.getCanonicalPath;
import static org.saucedemo.utils.FileUtils.getFileAsString;

// https://www.baeldung.com/jackson-object-mapper-tutorial
// Appium Desired Capabilities: https://appium.io/docs/en/writing-running-appium/caps/#appium-desired-capabilities
// Logging of capabilities: https://appiumpro.com/editions/10-anatomy-of-logging-in-appium

@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CapabilitiesFactory {
    private static Config config = EnvConfigFactory.getConfig();

    private static String platformName = config.getString("platformName").toLowerCase();
    private static String deviceType = config.getString("deviceType").toLowerCase();
    private static String deviceName = config.getString("deviceName").toLowerCase();
    private static String host = config.getString("host").toLowerCase();

    // Don't want to create any driver for this factory class.
    private CapabilitiesFactory(){

    }

    // Preferred way (since tests should care about testing application and not on which device). This is concern for capabilities class.
    public static DesiredCapabilities getDesiredCapabilities() {
        /*
         If user wants to pick any random device. Then get a random device.
         Else, keep the deviceName provided by user in application.conf file.
         Note: that if you do provide a fixed deviceName, then you cannot run tests in parallel.
         So change parallel property to false in junit-platform.properties file.
         junit.jupiter.execution.parallel.enabled=false (for parallel mode keep this true and deviceName = randomDevice
         */
        if (deviceName.equalsIgnoreCase("randomDevice")) {
            deviceName = getDevice();
        }

        return getDesiredCapabilities(deviceName);
    }

    // In case if in future, there is a need to get a capability from another calling class, we also provide a option for that.
    public static DesiredCapabilities getDesiredCapabilities(String deviceName) {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        // Capabilities are affected based on below 4 parameters.
        // Thus depending on what the choice from user is defined in application.conf file,
        // a valid capability is build for the tests.
        log.info("Running tests on platform: {}", platformName);
        log.info("Running tests on deviceType: {}", deviceType);
        log.info("Running tests on deviceName: {}", deviceName);
        log.info("Running tests on host: {}", host);
        switch (platformName) {
            case "android":
                // Common android capabilities here
                capabilities.setCapability("platformName", "Android");
                capabilities.setCapability("automationName", "UiAutomator2");
                capabilities.setCapability("appWaitActivity", "com.swaglabsmobileapp.MainActivity");

                // Capabilities specific for device type
                switch (deviceType) {
                    case "real":
                        // some code here
                    case "virtual":
                        // get default properties from android-emulator-capabilities.json
                        String pathAndroidEmulatorCapabilities = config.getString("pathAndroidEmulatorCapabilities").toLowerCase();
                        capabilities = setCapabilitiesFromFile(pathAndroidEmulatorCapabilities, capabilities);

                        // set unique systemPort and virtual device name from configuration files for the selected device
                        String pathDesiredCapabilities = config.getString("pathDesiredCapabilities").toLowerCase();
                        capabilities = setCapabilitiesFromFile(String.format("%s/%s.json", pathDesiredCapabilities, deviceName), capabilities);

                        // Set the avd property with the virtual drive that you have with you on your machine.
                        capabilities.setCapability("avd", deviceName);
                }

                // Capabilities specific for host
                switch (host) {
                    case "local":
                        // Get local app location stored in the project here (via absolute path)
                        String pathAndroidApp = config.getString("pathAndroidApp");
                        capabilities.setCapability("app", getCanonicalPath(pathAndroidApp));
                    case "browser-stack":
                        // Get app location on remote server here (via http)
                        // Note this property should be parsed from a config file and should not be set here (as done below).
                        // Since there can be multiple devices that are available to run on host and not just One.
                        // capabilities.setCapability("app", "bs://" + ANDROID_HASHED_APP_ID);
                }

                break;
            case "ios":
                // Common iOS capabilities here
                capabilities.setCapability("platformName", "ios");
                capabilities.setCapability("automationName", "XCUITest");
                capabilities.setCapability("deviceName", "iPhone 7");

                break;
            default:
                break;
        }

        log.info("Capabilities: {}", capabilities);
        return capabilities;
    }

    private static DesiredCapabilities setCapabilitiesFromFile(String filePath, DesiredCapabilities capabilities) {
        String jsonString = getFileAsString(filePath);

        JSONObject obj = new JSONObject(jsonString);
        Iterator<?> keys = obj.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String value = obj.get(key).toString();

            log.debug("key: {}", key);
            log.debug("value: {}", value);

            capabilities.setCapability(key, value);
        }

        return capabilities;
    }
}
