package factories;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;

import static factories.EnvConfigFactory.getConfig;

// This is what I need.
// https://www.baeldung.com/jackson-object-mapper-tutorial
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

    public static CapabilitiesFactory getCapabilities(String deviceName){
        Config config = getConfig();
        String pathDesiredCapabilities = config.getString("pathDesiredCapabilities");

        ObjectMapper objectMapper = new ObjectMapper();
        CapabilitiesFactory capabilitiesFactory = null;
        try {
            capabilitiesFactory = objectMapper.readValue(new File(String.format("%s/%s.json", pathDesiredCapabilities, deviceName)), CapabilitiesFactory.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return capabilitiesFactory;
    }
}
