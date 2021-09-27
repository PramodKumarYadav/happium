package org.saucedemo.factories.capabilities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.typesafe.config.Config;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.saucedemo.factories.EnvConfigFactory;

import static org.saucedemo.factories.capabilities.DeviceCapabilities.getDeviceCapabilities;
import static org.saucedemo.factories.capabilities.GeneralCapabilities.getGeneralCapabilities;

// https://www.baeldung.com/jackson-object-mapper-tutorial
// Appium Desired Capabilities: https://appium.io/docs/en/writing-running-appium/caps/#appium-desired-capabilities
// Logging of capabilites: https://appiumpro.com/editions/10-anatomy-of-logging-in-appium

@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Data
public class CapabilitiesFactory {
    Config config = EnvConfigFactory.getConfig();


    public DesiredCapabilities getDesiredCapabilities(String deviceName) {

        GeneralCapabilities generalCapabilities = getGeneralCapabilities();
        DeviceCapabilities deviceCapabilities = getDeviceCapabilities(deviceName);

        // get general capabilites
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("automationName", generalCapabilities.getAutomationName());
        capabilities.setCapability("platformName", generalCapabilities.getPlatformName());
        capabilities.setCapability("platformVersion", generalCapabilities.getPlatformVersion());
        capabilities.setCapability("app", generalCapabilities.getApp());
        capabilities.setCapability("appWaitActivity", generalCapabilities.getAppWaitActivity());

        // get drive specific capabilities
        capabilities.setCapability("deviceName", deviceCapabilities.getDeviceName());
        capabilities.setCapability("avd", deviceCapabilities.getAvd());
        capabilities.setCapability("avdLaunchTimeout", deviceCapabilities.getAvdLaunchTimeout());
        capabilities.setCapability("avdReadyTimeout", deviceCapabilities.getAvdReadyTimeout());

        return capabilities;
    }
}
