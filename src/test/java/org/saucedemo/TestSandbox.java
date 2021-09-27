import com.typesafe.config.Config;
import org.saucedemo.factories.CapabilitiesFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import static org.saucedemo.factories.EnvConfigFactory.getConfig;

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

        DesiredCapabilities capabilities = new CapabilitiesFactory().getDesiredCapabilities(deviceName);
        log.info(String.valueOf(capabilities));
    }
}
