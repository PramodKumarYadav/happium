package org.saucedemo.login;

import com.typesafe.config.Config;
import io.appium.java_client.AppiumDriver;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.saucedemo.factories.DriverFactory;
import org.saucedemo.factories.EnvConfigFactory;
import org.saucedemo.screens.LoginScreen;
import org.saucedemo.screens.ProductsScreen;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class TestLogin {
    AppiumDriver driver = DriverFactory.getDriver();

    LoginScreen loginScreen = new LoginScreen(driver);
    ProductsScreen productsScreen = new ProductsScreen(driver);

    @BeforeEach
    public void setUp() {
        Config config = EnvConfigFactory.getConfig();
        log.info("Running tests on platform: {}", config.getString("platformName").toLowerCase());
        log.info("Running tests on deviceType: {}", config.getString("deviceType").toLowerCase());
//        log.info("Running tests on deviceName: {}", config.getString("deviceName").toLowerCase());
        log.info("Running tests on host: {}", config.getString("host").toLowerCase());
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
        assertEquals("afdaf", productsScreen.getProductsText());
    }
}
