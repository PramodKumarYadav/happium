package org.saucedemo.login;

import io.appium.java_client.AppiumDriver;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.saucedemo.screens.LoginScreen;
import org.saucedemo.screens.ProductsScreen;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.saucedemo.factories.DriverFactory.getDriver;

@Slf4j
class TestLogin {
    String deviceName = "Pixel_XL_API_31";
    AppiumDriver driver = getDriver(deviceName);
//    AppiumDriver driver = DriverFactory.getDriver(deviceName);
    LoginScreen loginScreen = new LoginScreen(driver);
    ProductsScreen productsScreen = new ProductsScreen(driver);

    @BeforeEach
    public void setUp() {
        log.info("Running tests on device: {}", deviceName);
    }

    @AfterEach
    public void tearDown(){
        driver.quit();
    }

    @Test
    void searchContact() throws InterruptedException {
        loginScreen.setUserName("standard_user");
        loginScreen.setPassword("secret_sauce");
        loginScreen.tapLogin();
        assertEquals("afdaf", productsScreen.getProductsText());
    }
}
