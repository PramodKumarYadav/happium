package org.saucedemo.screens;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.support.PageFactory;
import org.saucedemo.actions.screen.Screen;
import org.saucedemo.actions.screen.ScreenFactory;
import org.saucedemo.factories.DriverFactory;

public class InitializeScreen {
    Screen screen;

    protected InitializeScreen() {
        PageFactory.initElements(new AppiumFieldDecorator(DriverFactory.getDriver()), this);
        screen = ScreenFactory.getScreen();
    }
}
