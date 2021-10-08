package org.saucedemo.screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.support.PageFactory;
import org.saucedemo.screens.actions.ScreenActions;

@Slf4j
@Data
public class ProductDescriptionScreen {
    ScreenActions screenActions;

    public ProductDescriptionScreen(AppiumDriver driver) {
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
        screenActions = new ScreenActions(driver);
    }

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[1]")
    @iOSXCUITFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[1]")
    private MobileElement productDescription;

    public String getProductDescription() {
        screenActions.waitUntilElementIsVisible(productDescription);
        return productDescription.getText();
    }
}
