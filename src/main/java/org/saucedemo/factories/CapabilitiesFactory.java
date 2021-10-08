package org.saucedemo.factories;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.typesafe.config.Config;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Iterator;

import static org.saucedemo.factories.devices.AvailableDevices.getAndroidEmulator;
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

    /*
     Capabilities are affected based on below 4 parameters defined in application.conf file.
     Thus depending on what the choice from user is defined in application.conf file,
     a valid capability is build for the tests.
     */
    private static String platformName = config.getString("platformName").toLowerCase();
    private static String deviceType = config.getString("deviceType").toLowerCase();
    private static String deviceName = config.getString("deviceName").toLowerCase();
    private static String host = config.getString("host").toLowerCase();

    // Don't want to create any driver for this factory class.
    private CapabilitiesFactory(){

    }

    // In case if in future, there is a need to get a capability from another calling class, we also provide a option for that.
    public static DesiredCapabilities getDesiredCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();

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
                        // that will do something with the device name we get above.
                    case "virtual":
                        capabilities = setAndroidEmulatorCapabilities(deviceName, capabilities);
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

    /*
    Apart from fetching devices in a synchronized way (in a parallel run mode), we also had this challenge
     that the next steps to use this fetched device name to get capabilities and setting up avd, were getting affected
     in the race condition of parallel threads competing.
     This is the reason, we have to encapsulate all three steps of:
     1) finding an available emulator.
     2) setting capabilities for this emulator.
     3) setting avd for this device name as a synchronized block.
     In absence of this, the same device was getting picked by multiple threads running in parallel.
    http://tutorials.jenkov.com/java-concurrency/synchronized.html
    */
    private static synchronized DesiredCapabilities setAndroidEmulatorCapabilities(String deviceName, DesiredCapabilities capabilities){
        // get default properties from android-emulator-capabilities.json
        String pathAndroidEmulatorCapabilities = config.getString("pathAndroidEmulatorCapabilities").toLowerCase();
        capabilities = setCapabilitiesFromFile(pathAndroidEmulatorCapabilities, capabilities);

        /*
        Pick a device (fixed or random) based on the choice provided in application.conf
        If user wants to pick any random device. Then get a random device.
        Else, keep the deviceName provided by user in application.conf file. In case if user wants to test with a specific device.
        Note: that if you do provide a fixed deviceName, then you cannot run tests in parallel.
        So change parallel property to false in junit-platform.properties file.
        junit.jupiter.execution.parallel.enabled=false (for parallel mode keep this true and deviceName = randomDevice
        */

        if (deviceName.equalsIgnoreCase("randomVirtualDevice")) {
            deviceName = getAndroidEmulator();
        }

        // Note: Getting the above name with deviceName is not enough with synchronized. You need synchronization in below steps too.
        // set unique systemPort and virtual device name from configuration files for the selected device
        String pathDesiredCapabilities = config.getString("pathDesiredCapabilities").toLowerCase();
        String pathDeviceNameConfig = String.format("%s/%s.json", pathDesiredCapabilities, deviceName);
        log.info("pathDeviceNameConfig: {}", pathDeviceNameConfig);
        capabilities = setCapabilitiesFromFile(pathDeviceNameConfig, capabilities);

        // Set the avd property with the virtual drive that you have with you on your machine.
        capabilities.setCapability("avd", deviceName);

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
