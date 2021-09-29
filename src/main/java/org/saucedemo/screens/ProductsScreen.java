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
public class ProductsScreen {
    ScreenActions screenActions;

    public ProductsScreen(AppiumDriver driver) {
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
        screenActions = new ScreenActions(driver);
    }

    @AndroidFindBy(accessibility = "test-PRODUCTS")
    @iOSXCUITFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-LOGIN\"]/android.widget.TextView")
    private MobileElement products;

    public Boolean isProductHeadingDisplayed() {
        screenActions.waitUntilElementIsVisible(products);
        return products.isDisplayed();
    }
}
