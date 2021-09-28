package org.saucedemo.screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

@Slf4j
@Data
public class ProductsScreen {
    private WebDriverWait wait;
    private static final Integer TIME_OUT_IN_SECONDS = 5;

    public ProductsScreen(AppiumDriver driver) {
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
        wait = new WebDriverWait(driver, TIME_OUT_IN_SECONDS);
    }

    @AndroidFindBy(accessibility = "test-PRODUCTS")
    @iOSXCUITFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-LOGIN\"]/android.widget.TextView")
    private MobileElement products;

    public String getProductsText() {
        return products.getText();
    }
}
