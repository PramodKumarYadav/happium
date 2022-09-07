package org.saucedemo.actions.screen;

import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;

import org.saucedemo.choices.Platform;
import org.saucedemo.factories.EnvFactory;

@Slf4j
public class ScreenFactory {
    private static final Config CONFIG = EnvFactory.getInstance().getConfig();
    private static final Platform PLATFORM = Platform.parse(CONFIG.getString("PLATFORM"));

    private ScreenFactory() {
        throw new IllegalStateException("Static factory class");
    }

    public static Screen getScreen() {
        switch (PLATFORM) {
            case ANDROID:
                return new AndroidScreen();
            case IOS:
                return new IOSScreen();
            default:
                throw new IllegalStateException(String.format("%s is not a valid platform choice. Pick your platform from %s.", PLATFORM, java.util.Arrays.asList(Platform.values())));
        }
    }
}
