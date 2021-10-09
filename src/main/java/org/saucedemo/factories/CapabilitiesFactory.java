package org.saucedemo.factories;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.typesafe.config.Config;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.saucedemo.factories.devices.Device;

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
                // Set common android capabilities here from the config file
                capabilities = setAndroidCommonCapabilities(capabilities);

                // Set capabilities specific for device type (fixed or virtual)
                switch (deviceType) {
                    case "real":
                        capabilities = setAndroidRealDeviceCapabilities(deviceName, capabilities);
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

    private static DesiredCapabilities setAndroidCommonCapabilities(DesiredCapabilities capabilities){
        // get default properties from android-emulator-capabilities.json
        String pathAndroidCommonCapabilities = config.getString("pathAndroidCommonCapabilities").toLowerCase();

        capabilities = setCapabilitiesFromFile(pathAndroidCommonCapabilities, capabilities);
        return capabilities;
    }

    // This is when you want to run tests on a Single real android device connected to your computer.
    // So no synchronized required (since tests will run in sequence). Remember to put the parallel run property to false in junit-platform.properties
    private static DesiredCapabilities setAndroidRealDeviceCapabilities(String deviceName, DesiredCapabilities capabilities){
        String pathAndroidCapabilities = config.getString("pathAndroidCapabilities").toLowerCase();
        String pathDeviceNameConfig = String.format("%s/%s.json", pathAndroidCapabilities, deviceName);

        capabilities = setCapabilitiesFromFile(pathDeviceNameConfig, capabilities);
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
        String pathAndroidEmulatorDefaultCapabilities = config.getString("pathAndroidEmulatorDefaultCapabilities").toLowerCase();
        capabilities = setCapabilitiesFromFile(pathAndroidEmulatorDefaultCapabilities, capabilities);

        /*
        Pick a device (fixed or random) based on the choice provided in application.conf
        If user wants to pick any random device. Then get a random device.
        Else, keep the deviceName provided by user in application.conf file. In case if user wants to test with a specific device.
        Note: that if you do provide a fixed deviceName, then you cannot run tests in parallel.
        So change parallel property to false in junit-platform.properties file.
        junit.jupiter.execution.parallel.enabled=false (for parallel mode keep this true and deviceName = randomDevice
        */

        Device device = new Device();
        if (deviceName.equalsIgnoreCase("randomVirtualDevice")) {
            device = getAndroidEmulator();
        }

        // Set the avd property with the virtual drive that you have with you on your machine.
        capabilities.setCapability("avd", device.getDeviceName());
        capabilities.setCapability("deviceName", device.getUdid());
        capabilities.setCapability("udid", device.getUdid());
        capabilities.setCapability("systemPort  ", device.getSystemPort());

        return capabilities;
    }

    private static DesiredCapabilities setCapabilitiesFromFile(String filePath, DesiredCapabilities capabilities) {
        log.info("parsing desired capabilities from: {}", filePath);

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
