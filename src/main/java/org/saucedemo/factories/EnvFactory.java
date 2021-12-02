package org.saucedemo.factories;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.saucedemo.enums.Host;

@Slf4j
// For future reference on this topic: https://github.com/lightbend/config
public class EnvFactory {
    private EnvFactory() {
        throw new IllegalStateException("Static factory class");
    }

    public static final Config getConfig() {
        // Load default properties (first from System properties and then from application.conf)
        Config baseConfig = ConfigFactory.load();
        String hostName = baseConfig.getString("HOST").toUpperCase();

        if(EnumUtils.isValidEnum(Host.class, hostName)){
            log.info("loading properties for host: {}", hostName);
            Config hostConfig = ConfigFactory.load(hostName);
            return hostConfig.withFallback(baseConfig);
        }else{
            throw new IllegalStateException(String.format("%s is not a valid host choice. Pick your host from %s", hostName, java.util.Arrays.asList(Host.values())));
        }
    }
}
