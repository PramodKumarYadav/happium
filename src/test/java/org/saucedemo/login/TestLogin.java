package org.saucedemo.login;

import io.appium.java_client.AppiumDriver;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.saucedemo.screens.LoginScreen;
import org.saucedemo.screens.ProductsScreen;

import java.lang.invoke.MethodHandles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.saucedemo.factories.DriverFactory.getDriver;

@Slf4j
class TestLogin {
    private static final String className = MethodHandles.lookup().lookupClass().getSimpleName();

    private AppiumDriver driver;
    private LoginScreen loginScreen;
    private ProductsScreen productsScreen;

    @BeforeEach
    public void setUp() {
        driver = getDriver(className);

        loginScreen = new LoginScreen(driver);
        productsScreen = new ProductsScreen(driver);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @ParameterizedTest(name = "Login user - {0}")
    @CsvSource({"standard_user,secret_sauce"
            ,"problem_user,secret_sauce"
    })
    void assertThatAValidUserCanLogin(String userName, String password) {
        loginScreen.login(userName, password);
        assertTrue(productsScreen.isProductHeadingDisplayed());
    }

    @Test
    void assertThatAWarningIsDisplayedToALockedOutUser() {
        loginScreen.login("locked_out_user", "secret_sauce");
        assertEquals("Hellas Pindakaas!", loginScreen.getErrorMessage());
    }
}
