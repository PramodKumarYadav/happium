package org.saucedemo.login;

import io.appium.java_client.AppiumDriver;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.saucedemo.factories.DriverFactory;
import org.saucedemo.screens.LoginScreen;
import org.saucedemo.screens.ProductsScreen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class TestLogin {
    private AppiumDriver driver;
    private LoginScreen loginScreen;
    private ProductsScreen productsScreen;

    @BeforeEach
    public void setUp() {
        driver = new DriverFactory().getDriverV1();

        loginScreen = new LoginScreen(driver);
        productsScreen = new ProductsScreen(driver);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    void assertThatAValidUserCanLogin() {
        loginScreen.login("standard_user", "secret_sauce");
        assertTrue(productsScreen.isProductHeadingDisplayed());
    }

    @Test
    void assertThatAWarningIsDisplayedToALockedOutUser() {
        loginScreen.login("locked_out_user", "secret_sauce");
        assertEquals("Hellas Pindakaas!", loginScreen.getErrorMessage());
    }
}
