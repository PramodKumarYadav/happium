package org.saucedemo.deeplink;

import com.google.common.collect.ImmutableMap;
import com.typesafe.config.Config;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.support.PageFactory;
import org.saucedemo.choices.Platform;
import org.saucedemo.factories.EnvFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// https://saucelabs.com/blog/how-to-automate-deep-link-testing-emulators-simulators-real-devices
// https://github.com/saucelabs/sample-app-mobile#deep-linking
// https://gist.github.com/biwkf/b0ebb9940e6341ed5e588f148b4381a8

@Slf4j
public class DeepLink {
    private static Config config = EnvFactory.getInstance().getConfig();
    private static final Platform PLATFORM = Platform.valueOfLabel(config.getString("PLATFORM_NAME"));

    AppiumDriver driver;

    public DeepLink(AppiumDriver driver) {
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
        this.driver = driver;
    }

    public static String setDeepLinkUrl(String endpoint, String parameters) {
        String url = String.format("%s/%s", endpoint, parameters);
        return url;
    }

    public void toScreen(String deepLinkURL) {
        log.info("deepLinkURL: {}", deepLinkURL);
        switch (PLATFORM) {
            case ANDROID:
                toAndroidScreen(deepLinkURL);
                break;
            case IOS:
                toIOSScreen(deepLinkURL);
                break;
            default:
                break;
        }
    }

    private void toAndroidScreen(String deepLinkURL) {
        Map<String, String> map = new HashMap<>();
        map.put("url", deepLinkURL);
        map.put("package", config.getString("PACKAGE_NAME"));

        driver.executeScript("mobile:deepLink", map);
    }

    /**
     * Curtsy: https://gist.github.com/biwkf/b0ebb9940e6341ed5e588f148b4381a8
     * @param deepLinkURL
     * Note: On a real device it is not possible to simply call driver.get("url"), doing so will open SIRI and query with "url" resulting in a search.
     * Instead we can take advantage of Safari's launch parameters and pass deep link URL as argument. Below is an example:
     */
    private void toIOSScreen(String deepLinkURL) {
        driver.executeScript("mobile: terminateApp", ImmutableMap.of("bundleId", "com.apple.mobilesafari"));

        // Create arguments to open in safari
        List args = new ArrayList();
        args.add("-U");
        args.add(deepLinkURL);

        // Open safari
        Map<String, Object> params = new HashMap<>();
        params.put("bundleId", "com.apple.mobilesafari");
        params.put("arguments", args);

        driver.executeScript("mobile: launchApp", params);
        driver.findElementByAccessibilityId("Open").click();
    }
}
