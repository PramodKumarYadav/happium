package org.saucedemo.factories.capabilities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.saucedemo.factories.EnvConfigFactory;

import java.io.File;
import java.io.IOException;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Data
// http://appium.io/docs/en/writing-running-appium/caps/#appium-desired-capabilities
public class DeviceCapabilities {
    private String deviceName;
    private String avd;
    private String avdLaunchTimeout;
    private String avdReadyTimeout;

    public static DeviceCapabilities getDeviceCapabilities(String deviceName){
        Config config = EnvConfigFactory.getConfig();
        String pathDesiredCapabilities = config.getString("pathDesiredCapabilities");

        ObjectMapper objectMapper = new ObjectMapper();

        DeviceCapabilities generalCapabilities = null;
        File file = new File(String.format("%s/%s.json", pathDesiredCapabilities, deviceName));

        try {
            generalCapabilities = objectMapper.readValue(file, DeviceCapabilities.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return generalCapabilities;
    }
}
