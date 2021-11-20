package org.saucedemo.login;

import io.appium.java_client.AppiumDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.saucedemo.factories.DriverFactory;
import org.saucedemo.screens.LoginScreen;
import org.saucedemo.screens.ProductsScreen;
import org.saucedemo.extensions.TestExecutionLifecycle;

import java.lang.invoke.MethodHandles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.saucedemo.extensions.TestResult.packUp;

@ExtendWith(TestExecutionLifecycle.class)
class TestLogin {
    private static final String CLASS_NAME = MethodHandles.lookup().lookupClass().getSimpleName();

    private AppiumDriver driver;
    private LoginScreen loginScreen;
    private ProductsScreen productsScreen;

    @BeforeEach
    public void setUp() {
        driver = DriverFactory.getDriver(CLASS_NAME);

        loginScreen = new LoginScreen(driver);
        productsScreen = new ProductsScreen(driver);
    }

    @AfterEach
    public void tearDown() {
        packUp(driver);
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
        assertEquals("Sorry, this user has been locked out.", loginScreen.getErrorMessage());
    }
}
