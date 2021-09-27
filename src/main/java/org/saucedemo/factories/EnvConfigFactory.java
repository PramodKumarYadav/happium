package factories;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class EnvConfigFactory {
    public static Config getConfig() {
        Config appConfig = ConfigFactory.load();

        // See if any host is passed from command line
        String host = System.getProperty("host");
        log.debug("host passed from command line: {}", host);

        // If host name is not passed from command line, choose it from application.config
        if (StringUtils.isEmpty(host)) {
            host = appConfig.getString("host");
        }

        // Load properties specific for chosen host.
        log.debug("host to load config: {}", host);
        System.setProperty("chosenHost", host);
        Config hostConfig = ConfigFactory.load(host);

        // Merge properties from common properties (application.conf) and host specific properties (from host file)
        Config mergedConfig = hostConfig.withFallback(appConfig);
        return mergedConfig;
    }
}
