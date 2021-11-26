package org.saucedemo.factories;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

//@Slf4j
// For future reference on this topic: https://github.com/lightbend/config
public class TestEnvironment {
    public static final Config getConfig() {
        // Load default properties (first from System properties and then from application.conf)
        Config baseConfig = ConfigFactory.load();
        String HOST = baseConfig.getString("HOST");

        if(HOST.equalsIgnoreCase("localhost") || HOST.equalsIgnoreCase("browserstack") || HOST.equalsIgnoreCase("saucelabs")){
            Config HOST_CONFIG = ConfigFactory.load(HOST);
            Config MERGED_CONFIG = HOST_CONFIG.withFallback(baseConfig);
            return MERGED_CONFIG;
        }else{
            throw new IllegalStateException(String.format("%s is not a valid host choice. Pick your host from localhost, browserstack or saucelabs", HOST));
        }
    }
}
