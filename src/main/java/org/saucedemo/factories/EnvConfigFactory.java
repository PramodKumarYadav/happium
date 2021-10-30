package org.saucedemo.factories;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

//@Slf4j
// For future reference on this topic: https://github.com/lightbend/config
public class EnvConfigFactory {
    // Load default properties (first from System properties and then from application.conf)
    private static final Config BASE_CONFIG = ConfigFactory.load();
    private static final String HOST = BASE_CONFIG.getString("HOST");
    private static final Config HOST_CONFIG = ConfigFactory.load(HOST);
    private static final Config MERGED_CONFIG = HOST_CONFIG.withFallback(BASE_CONFIG);

    public static final Config getConfig() {
        return MERGED_CONFIG;
    }
}
