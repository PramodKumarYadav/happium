package org.saucedemo.screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.support.PageFactory;
import org.saucedemo.actions.screen.Screen;
import org.saucedemo.actions.screen.ScreenFactory;

import java.util.List;

@Slf4j
@Data
public class ProductsScreen {
    Screen screen;

    public ProductsScreen(AppiumDriver driver) {
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
        screen = new ScreenFactory(driver).getScreen();
    }

    @AndroidFindBy(accessibility = "test-PRODUCTS")
    @iOSXCUITFindBy(accessibility = "PRODUCTS")
    private MobileElement productsTitle;

    @AndroidFindBy(accessibility = "test-Item title")
    @iOSXCUITFindBy(accessibility = "test-Item title")
    private List<MobileElement> productsSummary;

    @AndroidFindBy(accessibility = "test-Price")
    @iOSXCUITFindBy(accessibility = "test-Price")
    private List<MobileElement> productsPrice;

    public Boolean isProductHeadingDisplayed() {
        screen.waitUntilElementIsVisible(productsTitle);
        return productsTitle.isDisplayed();
    }

    public ProductsScreen clickProductNumber(Integer itemNumber) {
        screen.tap(productsSummary.get(itemNumber));
        return this;
    }

    public String getProductTitle(Integer itemNumber) {
        return screen.getText(productsSummary.get(itemNumber));
    }

    public String getProductPrice(Integer itemNumber) {
        return screen.getText(productsPrice.get(itemNumber));
    }
}
