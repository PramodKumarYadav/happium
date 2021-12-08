package org.saucedemo.factories;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.lang3.EnumUtils;
import org.saucedemo.enums.Host;

/**
 * Env configuration once loaded, is to remain constant for all classes using it. Thus we will follow Singleton design pattern here.
 * For future reference on this topic: https://github.com/lightbend/config
 */
public class EnvFactory {
    /**
     * With this approach, we are relying on JVM to create the unique instance of EnvFactory when the class is loaded.
     * The JVM guarantees that the instance will be created before any thread accesses the static uniqueInstance variable.
     * This code is thus guaranteed to be thread safe.
     */
    private static EnvFactory uniqueInstance = new EnvFactory();

    private EnvFactory() {
    }

    public static EnvFactory getInstance() {
        return uniqueInstance;
    }

    public Config getConfig() {
        // Load default properties (first from System properties and then from application.conf)
        Config baseConfig = ConfigFactory.load();
        String host = baseConfig.getString("HOST");

        // Override/load values from host if this is a valid host, else, throw error.
        if (EnumUtils.isValidEnum(Host.class, host)) {
            Config hostConfig = ConfigFactory.load(host);
            return hostConfig.withFallback(baseConfig);
        } else {
            throw new IllegalStateException(String.format("%s is not a valid host choice. Pick your host from %s", host, java.util.Arrays.asList(Host.values())));
        }
    }
}
