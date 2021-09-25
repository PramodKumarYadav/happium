package screens.search;

import com.typesafe.config.Config;
import io.appium.java_client.AppiumDriver;
import lombok.extern.slf4j.Slf4j;

import static factories.EnvConfigFactory.getConfig;

@Slf4j
public class SearchScreenFactory {
    private static Config config = getConfig();
    private static String platformName = config.getString("platformName");

    private SearchScreenFactory() {
        // Do not want people to create an instance of Factory but use its static getContractSearchPage method to get
        // the right page for the job using  searchPage = ContactSearchPageFactory.getContractSearchPage(deviceType, driver);
    }

    public static SearchScreen getSearchScreen(AppiumDriver driver){
        log.info("Platform to run the application: {}", platformName);
        switch (platformName.toLowerCase()) {
            case "ios":
                return new SearchScreenIOS(driver);
            case "android":
            default:
                return new SearchScreenAndroid(driver);
        }
    }
}
