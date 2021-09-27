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
public class GeneralCapabilities {
    private String automationName;
    private String platformName;
    private String platformVersion;
    private String deviceName;
    private String app;
    private String appWaitActivity;
//    private String otherApps;
//    private String browserName;
//    private String newCommandTimeout;
//    private String language;
//    private String udid;
//    private String orientation;
//    private String autoWebview;
//    private String noReset;
//    private String fullReset;
//    private String eventTimings;
//    private String enablePerformanceLogging;
//    private String printPageSourceOnFindFailure;
//    private String clearSystemFiles;

    public static GeneralCapabilities getGeneralCapabilities(){
        Config config = EnvConfigFactory.getConfig();
        String pathDesiredCapabilities = config.getString("pathDesiredCapabilities");
        String platformName = config.getString("platformName");

        ObjectMapper objectMapper = new ObjectMapper();
        GeneralCapabilities generalCapabilities = null;
        File file = new File(String.format("%s/%s-general-capabilities.json", pathDesiredCapabilities, platformName));
        try {
            generalCapabilities = objectMapper.readValue(file, GeneralCapabilities.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return generalCapabilities;
    }
}
