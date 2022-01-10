package org.saucedemo.screens;

import com.typesafe.config.Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.saucedemo.deeplink.DeepLink;
import org.saucedemo.testextensions.TestSetup;
import org.saucedemo.factories.EnvFactory;
import org.saucedemo.testextensions.TestExecutionLifecycle;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("android")
@Tag("ios")
@ExtendWith(TestExecutionLifecycle.class)
class TestProducts extends TestSetup {
    private static Config config = EnvFactory.getInstance().getConfig();
    private ProductsScreen productsScreen;
    private DeepLink deepLink;


    @BeforeEach
    public void initialize() {
        productsScreen = new ProductsScreen(driver);

        deepLink = new DeepLink(driver);
        String deepLinkUrl = DeepLink.getDeepLinkUrl(config.getString("SWAG_ITEMS_OVERVIEW"), "0,1");
        deepLink.toScreen(deepLinkUrl);
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
