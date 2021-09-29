package org.saucedemo.login;

import io.appium.java_client.AppiumDriver;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.saucedemo.factories.DriverFactory;
import org.saucedemo.screens.LoginScreen;
import org.saucedemo.screens.ProductsScreen;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class TestLogin {
    AppiumDriver driver = DriverFactory.getDriver();

    LoginScreen loginScreen = new LoginScreen(driver);
    ProductsScreen productsScreen = new ProductsScreen(driver);

    @BeforeEach
    public void setUp() {
        log.info("add anything that you need for your particular test here.");
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    void searchContact() {
        loginScreen.setUserName("standard_user");
        loginScreen.setPassword("secret_sauce");
        loginScreen.tapLogin();

        assertTrue(productsScreen.isProductHeadingDisplayed());
    }
}
