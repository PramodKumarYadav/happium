package org.saucedemo.actions.screen;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.WebElement;
import org.saucedemo.factories.DriverContext;

/**
 * All interactions in pages should happen via screen actions and not directly in the pages.
 * this class provides robustness and readability and reduces flakiness.
 */
public class IOSScreen extends Screen {
    private AppiumDriver driver;

    public IOSScreen() {
        driver = DriverContext.getDriver();
    }

    // todo: to be implemented
    public WebElement scrollToText(String partialText) {
        return driver.findElement(MobileBy.AndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textContains(\"" + partialText + "\").instance(0))"));
    }
}
