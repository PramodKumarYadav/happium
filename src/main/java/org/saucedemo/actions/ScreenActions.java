package org.saucedemo.actions;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * All interactions in pages should happen via screen actions and not directly in the pages.
 * this class provides robustness and readability and reduces flakiness.
 */
public class ScreenActions {
    AppiumDriver driver;
    private WebDriverWait shortWait;
    private WebDriverWait longWait;
    private static final Integer TIME_OUT_IN_FIVE_SECONDS = 5;
    private static final Integer TIME_OUT_IN_TEN_SECONDS = 10;

    public ScreenActions(AppiumDriver driver) {
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
        shortWait = new WebDriverWait(driver, TIME_OUT_IN_FIVE_SECONDS);
        longWait = new WebDriverWait(driver, TIME_OUT_IN_TEN_SECONDS);
        this.driver = driver;
    }

    public void setTextField(MobileElement mobileElement, String value) {
        waitUntilElementIsVisible(mobileElement);

        mobileElement.click();
        mobileElement.clear();
        mobileElement.sendKeys(value);
    }

    public WebElement waitUntilElementIsVisible(MobileElement mobileElement) {
        return longWait.until(ExpectedConditions.visibilityOf(mobileElement));
    }

    public void clickButton(MobileElement mobileElement) {
        waitUntilElementIsVisible(mobileElement).isEnabled();
        mobileElement.click();
    }

    public String getText(MobileElement mobileElement) {
        waitUntilElementIsVisible(mobileElement).isEnabled();
        return mobileElement.getText();
    }

    // todo: This is android specific method used only for demo purpose. Eventually replace this with a method that works
    //  on both android and ios.
    public WebElement scrollAndGetElementContainingText(String partialText) {
        return driver.findElement(MobileBy.AndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textContains(\"" + partialText + "\").instance(0))"));
    }
}
