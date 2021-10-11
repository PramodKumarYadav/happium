package org.saucedemo.deeplink;

import com.google.common.collect.ImmutableMap;
import com.typesafe.config.Config;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.support.PageFactory;
import org.saucedemo.factories.EnvConfigFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// https://saucelabs.com/blog/how-to-automate-deep-link-testing-emulators-simulators-real-devices
// https://github.com/saucelabs/sample-app-mobile#deep-linking
// https://gist.github.com/biwkf/b0ebb9940e6341ed5e588f148b4381a8

@Slf4j
public class DeepLink {
    AppiumDriver driver;
    private static Config config = EnvConfigFactory.getConfig();
    private static String platformName = config.getString("platformName");

    public DeepLink(AppiumDriver driver) {
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
        this.driver = driver;
    }

    public void deepLinkToScreen(String url, String packageName) {
        log.info("deepLinkURL: {}", url);
        log.info("packageName: {}", packageName);

        switch (platformName) {
            case "android":
                Map<String, String> map = new HashMap<>();
                map.put("url", url);
                map.put("package", packageName);

                driver.executeScript("mobile:deepLink", map);
                break;
            case "ios":
                // Todo: Untested code. Curtsy: https://gist.github.com/biwkf/b0ebb9940e6341ed5e588f148b4381a8
                /* On a real device it is not possible to simply call driver.get("url"), doing so will open SIRI and query with "url" resulting in a search.
                Instead we can take advantage of Safari's launch parameters and pass deep link URL as argument.
                Below is an example: */

                String deepLinkURL = "deeplink://";
                driver.executeScript("mobile: terminateApp", ImmutableMap.of("bundleId", "com.apple.mobilesafari"));

                List args = new ArrayList();
                args.add("-u");
                args.add(deepLinkURL);

                Map<String, Object> params = new HashMap<>();
                // Open safari
                params.put("bundleId", "com.apple.mobilesafari");
                params.put("arguments", args);

                driver.executeScript("mobile: launchApp", params);
                driver.findElementByAccessibilityId("Open").click();
                break;
            default:
                break;
        }
    }
}
