package org.saucedemo.factories;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.typesafe.config.Config;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Iterator;

import static org.saucedemo.utils.FileUtils.getCanonicalPath;
import static org.saucedemo.utils.FileUtils.getFileAsString;

// https://www.baeldung.com/jackson-object-mapper-tutorial
// Appium Desired Capabilities: https://appium.io/docs/en/writing-running-appium/caps/#appium-desired-capabilities
// Logging of capabilities: https://appiumpro.com/editions/10-anatomy-of-logging-in-appium

@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Data
public class CapabilitiesFactory {
    private static Config config = EnvConfigFactory.getConfig();
    private static String platformName = config.getString("platformName").toLowerCase();
    private static String deviceType = config.getString("deviceType").toLowerCase();
    // For device name, pass a fixed name or get from list of available devices (on local or remote).
    private static String deviceName = "Pixel_XL_API_31";
    private static String host = config.getString("host").toLowerCase();
    private static Integer systemPort = 8200;
    private static Integer serverPort = 5554;

    // Preferred and convenient way (since tests should care about testing application and not on which device).
    // That is concern for capabilities.
    public DesiredCapabilities getDesiredCapabilities() {
        return getDesiredCapabilities(deviceName);
    }

    // In case if in future, there is a need to get a capability from another calling class, we also provide a option for that.
    public DesiredCapabilities getDesiredCapabilities(String deviceName) {
        DesiredCapabilities capabilities = new DesiredCapabilities();

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

                        // Set port to a unique number and increment by one (so that next parallel run can get a new free systemPort)
                        capabilities.setCapability("systemPort", systemPort);
                        systemPort++;

                        // set virtual device name and the avd - device name to use to start this emulator.
                        capabilities.setCapability("deviceName", "emulator-" + serverPort);
                        serverPort = serverPort + 2;

                        // Set the avd property with the virtual drive that you have with you on your machine.
                        capabilities.setCapability("avd", deviceName);
                }

                // Capabilities specific for host
                switch (host) {
                    case "local":
                        // Get local app location stored in the project here (via absolute path)
                        String pathAndroidApp = config.getString("pathAndroidApp").toLowerCase();
                        capabilities.setCapability("app", getCanonicalPath(pathAndroidApp));
                    case "browser-stack":
                        // Get app location on remote server here (via http)
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

    private DesiredCapabilities setCapabilitiesFromFile(String filePath, DesiredCapabilities capabilities) {
        String jsonString = getFileAsString(filePath);

        JSONObject obj = new JSONObject(jsonString);
        Iterator<?> keys = obj.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String value = obj.get(key).toString();
            log.info("key: {}", key);
            log.info("value: {}", value);

            capabilities.setCapability(key, value);
        }

        return capabilities;
    }
}
