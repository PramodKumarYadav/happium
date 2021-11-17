package org.saucedemo.login;

import com.typesafe.config.Config;
import io.appium.java_client.AppiumDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.saucedemo.deeplink.DeepLink;
import org.saucedemo.factories.DriverFactory;
import org.saucedemo.factories.TestEnvironment;
import org.saucedemo.screens.ProductsScreen;
import org.saucedemo.testresults.RunnerExtension;

import java.lang.invoke.MethodHandles;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.saucedemo.deeplink.DeepLink.setDeepLinkUrl;
import static org.saucedemo.testresults.TestResult.packUp;

@ExtendWith(RunnerExtension.class)
class TestProducts {
    private static final String CLASS_NAME = MethodHandles.lookup().lookupClass().getSimpleName();
    private static final Config CONFIG = TestEnvironment.getConfig();

    private AppiumDriver driver;
    private ProductsScreen productsScreen;
    private DeepLink deepLink;

    @BeforeEach
    public void setUp() {
        driver = DriverFactory.getDriver(CLASS_NAME);
        productsScreen = new ProductsScreen(driver);

        deepLink = new DeepLink(driver);
        String url = setDeepLinkUrl(CONFIG.getString("SWAG_ITEMS_OVERVIEW"), "0,1");
        deepLink.toScreen(url);
    }

    @AfterEach
    public void tearDown() {
        packUp(driver);
    }

    @ParameterizedTest(name = "Product summary for product - {1}")
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
