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
import org.saucedemo.screens.ProductScreen;

import java.lang.invoke.MethodHandles;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.saucedemo.factories.DriverFactory.getDriver;

@Slf4j
class TestProduct {
    private static final String className = MethodHandles.lookup().lookupClass().getSimpleName();

    private AppiumDriver driver;
    private ProductScreen productScreen;
    private DeepLink deepLink;

    private static final Config config = EnvConfigFactory.getConfig();
    private static final String swagItemDetails = config.getString("swagItemDetails");
    private static final String packageName = config.getString("packageName");

    @BeforeEach
    public void setUp() {
        driver = getDriver(className);
        productScreen = new ProductScreen(driver);

        deepLink = new DeepLink(driver);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @ParameterizedTest(name = "Product details for product number - {0}")
    @CsvSource(value = {"0; Sauce Labs Backpack;carry.allTheThings() with the sleek, streamlined Sly Pack that melds uncompromising style with unequaled laptop and tablet protection."
            ,"1; Sauce Labs Bike Light;A red light isn't the desired state in testing but it sure helps when riding your bike at night. Water-resistant with 3 lighting modes, 1 AAA battery included."
    }, delimiter = ';')
    void assertThatProductDescriptionIsCorrectForAStandardUser(Integer productNumber, String productSummary, String productDescription) {
        String url = String.format("%s/%s", swagItemDetails, productNumber);
        deepLink.deepLinkToScreen(url , packageName);

        assertAll("Product Details"
                , () -> assertEquals(productSummary, productScreen.getProductSummary())
                , () -> assertEquals(productDescription, productScreen.getProductDescriptionByText(productDescription))
        );
    }
}
