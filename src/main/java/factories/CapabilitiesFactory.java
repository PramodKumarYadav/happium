package factories;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;

import static factories.EnvConfigFactory.getConfig;

// https://www.baeldung.com/jackson-object-mapper-tutorial
// Appium Desired Capabilities: https://appium.io/docs/en/writing-running-appium/caps/#appium-desired-capabilities
// Logging of capabilites: https://appiumpro.com/editions/10-anatomy-of-logging-in-appium

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CapabilitiesFactory {
    // Input fields (used in requests)
    private String automationName;
    private String platformName;
    private String platformVersion;
    private String deviceName;
    private String app;
    private String appPackage;
    private String appActivity;
    private String avd;
    private Integer avdLaunchTimeout;
    private Integer avdReadyTimeout;

    public DesiredCapabilities getDesiredCapabilities(String deviceName){
        DesiredCapabilities capabilities = new DesiredCapabilities();

        CapabilitiesFactory capabilitiesFactory = getCapabilities(deviceName);
        capabilities.setCapability("automationName", capabilitiesFactory.automationName);
        capabilities.setCapability("platformName", capabilitiesFactory.platformName);
        capabilities.setCapability("platformVersion", capabilitiesFactory.platformVersion);
        capabilities.setCapability("deviceName", capabilitiesFactory.deviceName);
        capabilities.setCapability("app", capabilitiesFactory.app);
        capabilities.setCapability("appPackage", capabilitiesFactory.appPackage);
        capabilities.setCapability("appActivity", capabilitiesFactory.appActivity);
        capabilities.setCapability("avd", capabilitiesFactory.avd);
        capabilities.setCapability("avdLaunchTimeout", capabilitiesFactory.avdLaunchTimeout);
        capabilities.setCapability("avdReadyTimeout", capabilitiesFactory.avdReadyTimeout);

        return capabilities;
    }
    
    private CapabilitiesFactory getCapabilities(String deviceName){
        Config config = getConfig();
        String pathDesiredCapabilities = config.getString("pathDesiredCapabilities");

        ObjectMapper objectMapper = new ObjectMapper();

        CapabilitiesFactory capabilitiesFactory = null;
        File file = new File(String.format("%s/%s.json", pathDesiredCapabilities, deviceName));
        try {
            capabilitiesFactory = objectMapper.readValue(file, CapabilitiesFactory.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return capabilitiesFactory;
    }
}
