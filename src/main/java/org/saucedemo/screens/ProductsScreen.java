package org.saucedemo.screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.support.PageFactory;
import org.saucedemo.commands.ScreenActions;

import java.util.List;

@Slf4j
@Data
public class ProductsScreen {
    ScreenActions screenActions;

    public ProductsScreen(AppiumDriver driver) {
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
        screenActions = new ScreenActions(driver);
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
        screenActions.waitUntilElementIsVisible(productsTitle);
        return productsTitle.isDisplayed();
    }

    public void clickProductNumber(Integer itemNumber) {
        screenActions.clickButton(productsSummary.get(itemNumber));
    }

    public String getProductTitle(Integer itemNumber) {
        return screenActions.getText(productsSummary.get(itemNumber));
    }

    public String getProductPrice(Integer itemNumber) {
        return screenActions.getText(productsPrice.get(itemNumber));
    }
}
