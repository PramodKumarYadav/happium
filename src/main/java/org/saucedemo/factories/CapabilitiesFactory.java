package org.saucedemo.factories;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.typesafe.config.Config;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.saucedemo.factories.devices.Device;

import java.util.Date;
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
    private static final Config CONFIG = EnvConfigFactory.getConfig();
    private static final Date ADD_DATE_TIME_TO_MAKE_BUILDS_UNIQUE = new Date();

    /*
     Capabilities are affected based on below 4 parameters defined in application.conf file.
     Thus depending on what the choice from user is defined in application.conf file,
     a valid capability is build for the tests.
     */
    private static final String HOST = CONFIG.getString("HOST");
    private static final String PLATFORM_NAME = CONFIG.getString("PLATFORM_NAME");
    private static final String DEVICE_TYPE = CONFIG.getString("DEVICE_TYPE");
    private static final String DEVICE_NAME = CONFIG.getString("DEVICE_NAME");

    // Don't want to create any driver for this factory class.
    private CapabilitiesFactory() {

    }

    // In case if in future, there is a need to get a capability from another calling class, we also provide a option for that.
    public static DesiredCapabilities getDesiredCapabilities(String testClassName) {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        log.info("Running tests for TestClass: {}", testClassName);
        log.info("Running tests on HOST: {}", HOST);

        // Capabilities specific for HOST
        switch (HOST) {
            case "localhost":
                // On localhost you are either on android or on IOS (not both).
                log.info("Running tests on PLATFORM_NAME: {}", PLATFORM_NAME);
                log.info("Running tests on DEVICE_TYPE: {}", DEVICE_TYPE);
                log.info("Running tests on DEVICE_NAME: {}", DEVICE_NAME);

                switch (PLATFORM_NAME) {
                    case "android":
                        // Get local app location stored in the project here (via absolute path)
                        capabilities.setCapability("app", getCanonicalPath(CONFIG.getString("PATH_ANDROID_APP")));

                        // Set common android capabilities here from the config file
                        capabilities = setAndroidCommonCapabilities(capabilities);

                        // Set capabilities specific for device type (fixed or virtual)
                        switch (DEVICE_TYPE) {
                            case "real":
                                capabilities = setAndroidRealDeviceCapabilities(DEVICE_NAME, capabilities);
                            case "virtual":
                                capabilities = setAndroidEmulatorCapabilities(capabilities, testClassName);
                        }
                        break;
                    case "ios":
                        // Get local app location stored in the project here (via absolute path)
                        capabilities.setCapability("app", getCanonicalPath(CONFIG.getString("PATH_IOS_APP")));

                        // Set common android capabilities here from the config file
                        capabilities = setIosCommonCapabilities(capabilities);

                        // Set capabilities specific for device type (fixed or virtual)
                        switch (DEVICE_TYPE) {
                            case "real":
                                capabilities = setIosRealDeviceCapabilities(DEVICE_NAME, capabilities);
                            case "virtual":
                                capabilities = setIosSimulatorCapabilities(capabilities);
                        }
                        break;
                    default:
                        break;
                }
                break;
            case "browserstack":
                // Note that browserstack user and key are fetched from system env variables. Rest all other properties are fetched from config.
                // Github does not allow dots in secrets. So I have to store these keys as underscores (different than
                // browserstack wants it to be.

                log.info("buildName: {}", CONFIG.getString("BROWSERSTACK_BUILD_NAME"));
                capabilities.setCapability("browserstack.user", System.getenv("BROWSERSTACK_USER"));
                capabilities.setCapability("browserstack.key", System.getenv("BROWSERSTACK_KEY"));
                capabilities.setCapability("app", CONFIG.getString("app"));

                capabilities.setCapability("project", CONFIG.getString("project"));
                capabilities.setCapability("build", CONFIG.getString("BROWSERSTACK_BUILD_NAME") + " - " + ADD_DATE_TIME_TO_MAKE_BUILDS_UNIQUE);
                capabilities.setCapability("name", testClassName);
                capabilities.setCapability("browserstack.networkLogs", true);

                capabilities.setCapability("device", CONFIG.getString("device"));
                capabilities.setCapability("os_version", CONFIG.getString("os_version"));
                break;
            default:
                log.info("Inside Host default");
                break;
        }

        log.debug("Capabilities: {}", capabilities);
        return capabilities;
    }

    private static DesiredCapabilities setAndroidCommonCapabilities(DesiredCapabilities capabilities) {
        capabilities = setCapabilitiesFromFile(CONFIG.getString("PATH_ANDROID_COMMON_CAPABILITIES"), capabilities);
        return capabilities;
    }

    private static DesiredCapabilities setIosCommonCapabilities(DesiredCapabilities capabilities) {
        capabilities = setCapabilitiesFromFile(CONFIG.getString("PATH_IOS_COMMON_CAPABILITIES"), capabilities);
        return capabilities;
    }

    // This is when you want to run tests on a Single real android device connected to your computer.
    // So no synchronized required (since tests will run in sequence). Remember to put the parallel run property to false in junit-platform.properties
    private static DesiredCapabilities setAndroidRealDeviceCapabilities(String deviceName, DesiredCapabilities capabilities) {
        String pathDeviceNameConfig = String.format("%s/%s.json", CONFIG.getString("PATH_ANDROID_CAPABILITIES"), deviceName);

        capabilities = setCapabilitiesFromFile(pathDeviceNameConfig, capabilities);
        return capabilities;
    }

    private static DesiredCapabilities setIosRealDeviceCapabilities(String deviceName, DesiredCapabilities capabilities) {
        String pathDeviceNameConfig = String.format("%s/%s.json", CONFIG.getString("PATH_IOS_CAPABILITIES"), deviceName);

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
    private static synchronized DesiredCapabilities setAndroidEmulatorCapabilities(DesiredCapabilities capabilities, String testClassName) {
        capabilities = setCapabilitiesFromFile(CONFIG.getString("PATH_ANDROID_EMULATOR_DEFAULT_CAPABILITIES"), capabilities);

        // getAndroidEmulator method contains logic to decide if user wants a 'specific' device or a 'random' device.
        // or "unique devices per test" within one class OR "unique device per each test class".
        Device device = getAndroidEmulator(testClassName);
        capabilities.setCapability("avd", device.getDeviceName());
        capabilities.setCapability("udid", device.getUdid());
        capabilities.setCapability("deviceName", device.getUdid());
        capabilities.setCapability("appium:systemPort  ", device.getSystemPort());

        return capabilities;
    }

    // todo: when you pick up IOS work.
    private static synchronized DesiredCapabilities setIosSimulatorCapabilities(DesiredCapabilities capabilities) {
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
