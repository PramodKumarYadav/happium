package org.saucedemo.factories;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.saucedemo.choices.Host;

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
        // Load default properties (first from System properties and then from application.conf file under main -> resources folder)
        Config baseConfig = ConfigFactory.load();
        String host = baseConfig.getString("HOST");

        // assert that the host value we fetched from application.conf is actually a valid Host choice specified in Host enum class.
        Host.valueOfLabel(host);

        /* Assumption is, if you specified this value in host, you have also created a valid hostname.conf file under main -> resources folder.
            for inspiration; refer say file localhost.conf or browserstack.conf
         */
        Config hostConfig = ConfigFactory.load(host);
        return hostConfig.withFallback(baseConfig);
    }
}
