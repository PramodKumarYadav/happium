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
    private static String host = config.getString("host");
    private static String platformName = config.getString("platformName");
    private static String deviceType = config.getString("deviceType");
    private static String deviceName = config.getString("deviceName");

    // Don't want to create any driver for this factory class.
    private CapabilitiesFactory(){

    }

    // In case if in future, there is a need to get a capability from another calling class, we also provide a option for that.
    public static DesiredCapabilities getDesiredCapabilities(String testClassName) {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        log.info("Running tests for TestClass: {}", testClassName);
        log.info("Running tests on host: {}", host);
        log.info("Running tests on platform: {}", platformName);
        log.info("Running tests on deviceType: {}", deviceType);
        log.info("Running tests on deviceName: {}", deviceName);

        // Capabilities specific for host
        switch (host) {
            case "local":
                // On localhost you are either on android or on IOS (not both).
                switch (platformName) {
                    case "android":
                        // Get local app location stored in the project here (via absolute path)
                        String pathAndroidApp = config.getString("pathAndroidApp");
                        capabilities.setCapability("app", getCanonicalPath(pathAndroidApp));

                        // Set common android capabilities here from the config file
                        capabilities = setAndroidCommonCapabilities(capabilities);

                        // Set capabilities specific for device type (fixed or virtual)
                        switch (deviceType) {
                            case "real":
                                capabilities = setAndroidRealDeviceCapabilities(deviceName, capabilities);
                            case "virtual":
                                capabilities = setAndroidEmulatorCapabilities(capabilities, testClassName);
                        }
                        break;
                    case "ios":
                        // Get local app location stored in the project here (via absolute path)
                        String pathIOSApp = config.getString("pathIOSApp");
                        capabilities.setCapability("app", getCanonicalPath(pathIOSApp));

                        // Set common android capabilities here from the config file
                        capabilities = setIosCommonCapabilities(capabilities);

                        // Set capabilities specific for device type (fixed or virtual)
                        switch (deviceType) {
                            case "real":
                                capabilities = setIosRealDeviceCapabilities(deviceName, capabilities);
                            case "virtual":
                                capabilities = setIosSimulatorCapabilities(capabilities);
                        }
                        break;
                    default:
                        break;
                }
            case "browserstack":
                // Note that browserstack user and key are fetched from system env variables. Rest all other properties are fetched from config.
                capabilities.setCapability("browserstack.user", System.getenv("browserstack.user"));
                capabilities.setCapability("browserstack.key", System.getenv("browserstack.key"));
                capabilities.setCapability("app", config.getString("app"));

                capabilities.setCapability("project", config.getString("project"));
                capabilities.setCapability("build", config.getString("build"));
                capabilities.setCapability("name", testClassName);
                capabilities.setCapability("browserstack.networkLogs", true);

                capabilities.setCapability("device", config.getString("device"));
                capabilities.setCapability("os_version", config.getString("os_version"));
                break;
            default:
                break;
        }

        log.info("Capabilities: {}", capabilities);
        return capabilities;
    }

    private static DesiredCapabilities setAndroidCommonCapabilities(DesiredCapabilities capabilities){
        // get default properties from android-emulator-capabilities.json
        String pathAndroidCommonCapabilities = config.getString("pathAndroidCommonCapabilities");

        capabilities = setCapabilitiesFromFile(pathAndroidCommonCapabilities, capabilities);
        return capabilities;
    }

    private static DesiredCapabilities setIosCommonCapabilities(DesiredCapabilities capabilities){
        // get default properties from ios-common-capabilities.json
        String pathIOSCommonCapabilities = config.getString("pathIOSCommonCapabilities");

        capabilities = setCapabilitiesFromFile(pathIOSCommonCapabilities, capabilities);
        return capabilities;
    }

    // This is when you want to run tests on a Single real android device connected to your computer.
    // So no synchronized required (since tests will run in sequence). Remember to put the parallel run property to false in junit-platform.properties
    private static DesiredCapabilities setAndroidRealDeviceCapabilities(String deviceName, DesiredCapabilities capabilities){
        String pathAndroidCapabilities = config.getString("pathAndroidCapabilities");
        String pathDeviceNameConfig = String.format("%s/%s.json", pathAndroidCapabilities, deviceName);

        capabilities = setCapabilitiesFromFile(pathDeviceNameConfig, capabilities);
        return capabilities;
    }

    private static DesiredCapabilities setIosRealDeviceCapabilities(String deviceName, DesiredCapabilities capabilities){
        String pathIOSCapabilities = config.getString("pathIOSCapabilities");
        String pathDeviceNameConfig = String.format("%s/%s.json", pathIOSCapabilities, deviceName);

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
    private static synchronized DesiredCapabilities setAndroidEmulatorCapabilities(DesiredCapabilities capabilities, String testClassName){
        // get default properties from android-emulator-capabilities.json
        String pathAndroidEmulatorDefaultCapabilities = config.getString("pathAndroidEmulatorDefaultCapabilities");
        capabilities = setCapabilitiesFromFile(pathAndroidEmulatorDefaultCapabilities, capabilities);

        // getAndroidEmulator method contains logic to decide if user wants a 'specific' device or a 'random' device.
        // or "unique devices per test" within one class OR "unique device per each test class".
        Device device = getAndroidEmulator(testClassName);
        capabilities.setCapability("avd", device.getDeviceName());
        capabilities.setCapability("udid", device.getUdid());
        capabilities.setCapability("deviceName", device.getUdid());
        capabilities.setCapability("systemPort  ", device.getSystemPort());

        return capabilities;
    }

    // todo: when you pick up IOS work.
    private static synchronized DesiredCapabilities setIosSimulatorCapabilities(DesiredCapabilities capabilities){
        return null;
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
