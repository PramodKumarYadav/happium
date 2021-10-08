package org.saucedemo.login;

import io.appium.java_client.AppiumDriver;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.saucedemo.screens.LoginScreen;
import org.saucedemo.screens.ProductDescriptionScreen;
import org.saucedemo.screens.ProductsScreen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.saucedemo.factories.DriverFactory.getDriver;

@Slf4j
class TestLogin {
    private AppiumDriver driver;
    private LoginScreen loginScreen;
    private ProductsScreen productsScreen;
    private ProductDescriptionScreen productDescriptionScreen;

    @BeforeEach
    public void setUp() {
        driver = getDriver();

        loginScreen = new LoginScreen(driver);
        productsScreen = new ProductsScreen(driver);
        productDescriptionScreen = new ProductDescriptionScreen(driver);
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

    @Test
    void assertThatAWarningIsDisplayedForAProblemUser() {
        loginScreen.login("problem_user", "secret_sauce");
        productsScreen.clickProductNumber(1);
        assertEquals( "Sauce Labs Bolt T-Shirt",productDescriptionScreen.getProductDescription());
    }
}
