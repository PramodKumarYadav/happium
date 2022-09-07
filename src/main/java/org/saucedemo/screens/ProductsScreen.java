package org.saucedemo.screens;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Data
public class ProductsScreen extends InitializeScreen {
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
