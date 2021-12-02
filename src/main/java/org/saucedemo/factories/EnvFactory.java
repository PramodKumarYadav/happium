package org.saucedemo.factories;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.lang3.EnumUtils;
import org.saucedemo.enums.Host;

//@Slf4j
// For future reference on this topic: https://github.com/lightbend/config
public class EnvFactory {
    public static final Config getConfig() {
        // Load default properties (first from System properties and then from application.conf)
        Config baseConfig = ConfigFactory.load();
        String hostName = baseConfig.getString("HOST").toUpperCase();

        if(EnumUtils.isValidEnum(Host.class, hostName)){
            Config HOST_CONFIG = ConfigFactory.load(hostName);
            Config MERGED_CONFIG = HOST_CONFIG.withFallback(baseConfig);
            return MERGED_CONFIG;
        }else{
            throw new IllegalStateException(String.format("%s is not a valid host choice. Pick your host from %s", hostName, java.util.Arrays.asList(Host.values())));
        }
    }
}
