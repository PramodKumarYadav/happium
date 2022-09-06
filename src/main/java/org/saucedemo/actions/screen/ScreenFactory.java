package org.saucedemo.actions.screen;

import com.typesafe.config.Config;
import io.appium.java_client.AppiumDriver;
import lombok.extern.slf4j.Slf4j;

import org.saucedemo.choices.Platform;
import org.saucedemo.factories.EnvFactory;

@Slf4j
public class ScreenFactory {
    private static final Config CONFIG = EnvFactory.getInstance().getConfig();
    private AppiumDriver driver;
    private static final Platform PLATFORM = Platform.parse(CONFIG.getString("PLATFORM"));

    public ScreenFactory(AppiumDriver driver) {
        this.driver = driver;
    }

    public Screen getScreen() {
        switch (PLATFORM) {
            case ANDROID:
                return new AndroidScreen(driver);
            case IOS:
                // or return an IOS screen.
            default:
                throw new IllegalStateException(String.format("%s is not a valid platform choice. Pick your platform from %s.", PLATFORM, java.util.Arrays.asList(Platform.values())));
        }
    }
}
