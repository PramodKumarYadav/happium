import com.typesafe.config.Config;
import factories.CapabilitiesFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static factories.EnvConfigFactory.getConfig;

@Slf4j
public class TestSandbox {
    private static Config config = getConfig();
    private static String hostURI = config.getString("hostURI");
    private static String serverPath = config.getString("serverPath");
    private static String deviceName = "Pixel_5_API_31";

    @Test
    void assertThatConfigFactoryIsWorkingAsDesired() {
        log.info(hostURI);
        log.info(serverPath);

        CapabilitiesFactory capabilitiesFactory = CapabilitiesFactory.getCapabilities(deviceName);
        log.info(capabilitiesFactory.getAutomationName());
        log.info(capabilitiesFactory.getDeviceName());
    }
}
