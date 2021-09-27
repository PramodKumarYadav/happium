package org.saucedemo;

import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.saucedemo.factories.capabilities.DeviceCapabilities;
import org.saucedemo.factories.capabilities.GeneralCapabilities;

import static org.saucedemo.factories.EnvConfigFactory.getConfig;
import static org.saucedemo.factories.capabilities.DeviceCapabilities.getDeviceCapabilities;
import static org.saucedemo.factories.capabilities.GeneralCapabilities.getGeneralCapabilities;

@Slf4j
public class TestSandbox {
    private static Config config = getConfig();
    private static String hostURI = config.getString("hostURI");
    private static String serverPath = config.getString("serverPath");
    private static String deviceName = "Pixel_XL_API_31";

    @Test
    void assertThatConfigFactoryIsWorkingAsDesired() {
        log.info(hostURI);
        log.info(serverPath);

        DeviceCapabilities capabilities = getDeviceCapabilities(deviceName);
        log.info(String.valueOf(capabilities));

        GeneralCapabilities generalCapabilities = getGeneralCapabilities();
        log.info(String.valueOf(generalCapabilities));
    }
}
