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
import org.saucedemo.screens.ProductScreen;
import org.saucedemo.extensions.TestExecutionLifecycle;

import java.lang.invoke.MethodHandles;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.saucedemo.deeplink.DeepLink.setDeepLinkUrl;
import static org.saucedemo.extensions.TestResult.packUp;

@ExtendWith(TestExecutionLifecycle.class)
class TestProduct {
    private static final String CLASS_NAME = MethodHandles.lookup().lookupClass().getSimpleName();
    private static final Config CONFIG = TestEnvironment.getConfig();

    private AppiumDriver driver;
    private ProductScreen productScreen;
    private DeepLink deepLink;

    @BeforeEach
    public void setUp() {
        driver = DriverFactory.getDriver(CLASS_NAME);
        productScreen = new ProductScreen(driver);

        deepLink = new DeepLink(driver);
    }

    @AfterEach
    public void tearDown() {
        packUp(driver);
    }

    @ParameterizedTest(name = "Product details for product - {1}")
    @CsvSource(value = {"0; Sauce Labs Backpack;carry.allTheThings() with the sleek, streamlined Sly Pack that melds uncompromising style with unequaled laptop and tablet protection."
            ,"1; Sauce Labs Bike Light;A red light isn't the desired state in testing but it sure helps when riding your bike at night. Water-resistant with 3 lighting modes, 1 AAA battery included."
    }, delimiter = ';')
    void assertThatProductDescriptionIsCorrectForAStandardUser(String productNumber, String productSummary, String productDescription) {
        String url = setDeepLinkUrl(CONFIG.getString("SWAG_ITEM_DETAILS"), productNumber);
        deepLink.toScreen(url);

        assertAll("Product Details"
                , () -> assertEquals(productSummary, productScreen.getProductSummary())
                , () -> assertEquals(productDescription, productScreen.getProductDescriptionByText(productDescription))
        );
    }
}
