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

@Slf4j
@Data
public class ProductScreen {
    Screen screen;

    public ProductScreen(AppiumDriver driver) {
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
        screen = new ScreenFactory(driver).getScreen();
    }

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[1]")
    @iOSXCUITFindBy(accessibility = "Sauce Labs Backpack")
    private MobileElement productSummary;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[2]")
    @iOSXCUITFindBy(accessibility = "carry.allTheThings() with the sleek, streamlined Sly Pack that melds uncompromising style with unequaled laptop and tablet protection.")
    private MobileElement productDescription;

    public String getProductSummary() {
        screen.waitUntilElementIsVisible(productSummary);
        return productSummary.getText();
    }

    public String getProductDescriptionByText(String partialText) {
        screen.scrollToText(partialText);
        screen.waitUntilElementIsVisible(productDescription);
        return productDescription.getText();
    }
}
