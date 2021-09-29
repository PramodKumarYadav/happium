package org.saucedemo.login;

import io.appium.java_client.AppiumDriver;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.saucedemo.factories.DriverFactory;
import org.saucedemo.screens.LoginScreen;
import org.saucedemo.screens.ProductsScreen;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.saucedemo.factories.EnvConfigFactory.getTestSetupDetails;

@Slf4j
class TestLogin {
    AppiumDriver driver = DriverFactory.getDriver();

    LoginScreen loginScreen = new LoginScreen(driver);
    ProductsScreen productsScreen = new ProductsScreen(driver);

    @BeforeAll
    public static void info() {
        getTestSetupDetails();
    }

    @BeforeEach
    public void setUp() {
        log.info("add any logging related info or device that we pick for each test here.");
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
