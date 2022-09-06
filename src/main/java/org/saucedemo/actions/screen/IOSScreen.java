package org.saucedemo.actions.screen;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * All interactions in pages should happen via screen actions and not directly in the pages.
 * this class provides robustness and readability and reduces flakiness.
 */
public class IOSScreen extends Screen {
    private AppiumDriver driver;
    private WebDriverWait shortWait;
    private WebDriverWait longWait;
    private static final Integer TIME_OUT_IN_FIVE_SECONDS = 5;
    private static final Integer TIME_OUT_IN_TEN_SECONDS = 10;

    public IOSScreen(AppiumDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        shortWait = new WebDriverWait(driver, TIME_OUT_IN_FIVE_SECONDS);
        longWait = new WebDriverWait(driver, TIME_OUT_IN_TEN_SECONDS);
        this.driver = driver;
    }

    // todo: to be implemented
    public WebElement scrollToText(String partialText) {
        return driver.findElement(MobileBy.AndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textContains(\"" + partialText + "\").instance(0))"));
    }
}
