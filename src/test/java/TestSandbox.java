import com.typesafe.config.Config;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static factories.TestConfigFactory.getConfig;
import static org.apache.http.HttpStatus.SC_OK;

@Slf4j
public class TestSandbox {
    private static Config config = getConfig();
    private static String hostURI = config.getString("hostURI");
    private static String serverPath = config.getString("serverPath");

    @Test
    void assertThatConfigFactoryIsWorkingAsDesired() {
        log.info(hostURI);
        log.info(serverPath);
    }
}
