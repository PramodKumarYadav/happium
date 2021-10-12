package org.saucedemo.login;

import com.typesafe.config.Config;
import io.appium.java_client.AppiumDriver;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.saucedemo.deeplink.DeepLink;
import org.saucedemo.factories.EnvConfigFactory;
import org.saucedemo.screens.ProductsScreen;

import java.lang.invoke.MethodHandles;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.saucedemo.deeplink.DeepLink.setDeepLinkUrl;
import static org.saucedemo.factories.DriverFactory.getDriver;

@Slf4j
class TestProducts {
    private static final String className = MethodHandles.lookup().lookupClass().getSimpleName();
    private static final Config config = EnvConfigFactory.getConfig();

    private AppiumDriver driver;
    private ProductsScreen productsScreen;
    private DeepLink deepLink;

    @BeforeEach
    public void setUp() {
        driver = getDriver(className);
        productsScreen = new ProductsScreen(driver);

        deepLink = new DeepLink(driver);
        String url = setDeepLinkUrl(config.getString("swagItemsOverview"), "0,1");
        deepLink.toScreen(url);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @ParameterizedTest(name = "Product details for product number - {0}")
    @CsvSource({"0, Sauce Labs Backpack,$29.99"
            , "1, Sauce Labs Bike Light,$9.99"
    })
    void assertThatProductDescriptionIsCorrectForAStandardUser(String productNumber, String productSummary, String productPrice) {
        assertAll("Products Overview"
                , () -> assertEquals(productSummary, productsScreen.getProductTitle(Integer.valueOf(productNumber)))
                , () -> assertEquals(productPrice, productsScreen.getProductPrice(Integer.valueOf(productNumber)))
        );
    }
}
