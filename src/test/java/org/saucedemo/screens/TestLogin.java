package org.saucedemo.screens;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.saucedemo.extensions.TestSetup;
import org.saucedemo.extensions.TestExecutionLifecycle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(TestExecutionLifecycle.class)
class TestLogin extends TestSetup {
    private LoginScreen loginScreen;
    private ProductsScreen productsScreen;

    @BeforeEach
    public void initialize() {
        loginScreen = new LoginScreen(driver);
        productsScreen = new ProductsScreen(driver);
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
    @DisplayName("Login user - locked_out_user")
    void assertThatAWarningIsDisplayedToALockedOutUser() {
        loginScreen.login("locked_out_user", "secret_sauce");
        assertEquals("Sorry, this user has been locked out.", loginScreen.getErrorMessage());
    }
}
