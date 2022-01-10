package org.saucedemo.screens;

import com.typesafe.config.Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.saucedemo.commands.DeepLink;
import org.saucedemo.testextensions.TestSetup;
import org.saucedemo.factories.EnvFactory;
import org.saucedemo.testextensions.TestExecutionLifecycle;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("android")
@ExtendWith(TestExecutionLifecycle.class)
class TestProduct extends TestSetup {
    private static Config config = EnvFactory.getInstance().getConfig();
    private ProductScreen productScreen;
    private DeepLink deepLink;

    @BeforeEach
    public void initialize() {
        productScreen = new ProductScreen(driver);
        deepLink = new DeepLink(driver);
    }

    @ParameterizedTest(name = "Product details for product - {1}")
    @CsvSource(value = {"0; Sauce Labs Backpack;carry.allTheThings() with the sleek, streamlined Sly Pack that melds uncompromising style with unequaled laptop and tablet protection."
            ,"1; Sauce Labs Bike Light;A red light isn't the desired state in testing but it sure helps when riding your bike at night. Water-resistant with 3 lighting modes, 1 AAA battery included."
    }, delimiter = ';')
    void assertThatProductDescriptionIsCorrectForAStandardUser(String productNumber, String productSummary, String productDescription) {
        String deepLinkUrl = DeepLink.getDeepLinkUrl(config.getString("SWAG_ITEM_DETAILS"), productNumber);
        deepLink.toScreen(deepLinkUrl);

        assertAll("Product Details"
                , () -> assertEquals(productSummary, productScreen.getProductSummary())
                , () -> assertEquals(productDescription, productScreen.getProductDescriptionByText(productDescription))
        );
    }
}
