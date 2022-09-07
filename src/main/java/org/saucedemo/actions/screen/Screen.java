package org.saucedemo.actions.screen;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.saucedemo.factories.DriverFactory;

public abstract class Screen {
    public abstract WebElement scrollToText(String partialText);

    AppiumDriver driver;
    private WebDriverWait longWait;
    private static final Integer TIME_OUT_IN_TEN_SECONDS = 10;

    public Screen() {
        driver = DriverFactory.getDriver();
        longWait = new WebDriverWait(driver, TIME_OUT_IN_TEN_SECONDS);
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

    public void tap(MobileElement mobileElement) {
        waitUntilElementIsVisible(mobileElement).isEnabled();
        mobileElement.click();
    }

    public String getText(MobileElement mobileElement) {
        waitUntilElementIsVisible(mobileElement).isEnabled();
        return mobileElement.getText();
    }
}
