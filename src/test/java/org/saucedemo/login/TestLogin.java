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
import static org.saucedemo.factories.EnvConfigFactory.getTestSetupDetails;

@Slf4j
class TestLogin {
    AppiumDriver driver;

    LoginScreen loginScreen;
    ProductsScreen productsScreen;

    @BeforeEach
    public void setUp() {
        getTestSetupDetails();

        driver = DriverFactory.getDriver();
        loginScreen = new LoginScreen(driver);
        productsScreen = new ProductsScreen(driver);
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
