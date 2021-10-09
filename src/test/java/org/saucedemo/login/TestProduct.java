package org.saucedemo.login;

import io.appium.java_client.AppiumDriver;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.saucedemo.screens.LoginScreen;
import org.saucedemo.screens.ProductScreen;
import org.saucedemo.screens.ProductsScreen;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.saucedemo.factories.DriverFactory.getDriver;

@Slf4j
class TestProduct {
    private AppiumDriver driver;
    private LoginScreen loginScreen;
    private ProductsScreen productsScreen;
    private ProductScreen productScreen;

    @BeforeEach
    public void setUp() {
        driver = getDriver();

        loginScreen = new LoginScreen(driver);
        productsScreen = new ProductsScreen(driver);
        productScreen = new ProductScreen(driver);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    void assertThatProductDescriptionIsCorrectForAStandardUser() {
        loginScreen.login("standard_user", "secret_sauce");
        productsScreen.clickProductNumber(0);

        String expectedProductSummary = "Sauce Labs Backpack";
        String expectedProductDescription = "carry.allTheThings() with the sleek, streamlined Sly Pack that melds uncompromising style with unequaled laptop and tablet protection.";
        assertAll("Product Details"
                , () -> assertEquals(expectedProductSummary, productScreen.getProductSummary())
                , () -> assertEquals(expectedProductDescription, productScreen.getProductDescriptionByText("streamlined Sly Pack"))
        );
    }

    @Test
    void assertThatProductDescriptionIsIncorrectForAProblemUser() {
        loginScreen.login("problem_user", "secret_sauce");
        productsScreen.clickProductNumber(0);

        String expectedProductSummary = "Sauce Labs Bike Light";
        String expectedProductDescription = "A red light isn't the desired state in testing but it sure helps when riding your bike at night. Water-resistant with 3 lighting modes, 1 AAA battery included.";
        assertAll("Product Details"
                , () -> assertEquals(expectedProductSummary, productScreen.getProductSummary())
                , () -> assertEquals(expectedProductDescription, productScreen.getProductDescriptionByText("A red light"))
        );
    }
}
