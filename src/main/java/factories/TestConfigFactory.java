package factories;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class TestConfigFactory {
    public static Config getConfig() {
        Config appConfig = ConfigFactory.load();

        // See if any environment is passed from command line
        String environment = System.getProperty("env");
        log.debug("environment passed from command line: {}", environment);

        // If host name is not passed from command line, choose it from application.config
        if (StringUtils.isEmpty(environment)) {
            environment = appConfig.getString("env");
        }

        // Load properties specific for chosen host.
        log.debug("environment to load config: {}", environment);
        System.setProperty("chosenEnv", environment);
        Config hostConfig = ConfigFactory.load(environment);

        // Merge properties from common properties (application.conf) and host specific properties (from host file)
        Config mergedConfig = hostConfig.withFallback(appConfig);
        return mergedConfig;
    }
}
