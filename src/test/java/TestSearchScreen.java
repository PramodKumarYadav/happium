import factories.DriverFactory;
import io.appium.java_client.AppiumDriver;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import screens.search.SearchScreen;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static screens.search.SearchScreenFactory.getSearchScreen;

@Slf4j
public class TestSearchScreen {
    String deviceName = "Pixel_XL_API_31";
    AppiumDriver driver = DriverFactory.getDriver(deviceName);
    SearchScreen searchScreen = getSearchScreen(driver);

    @BeforeEach
    public void setUp() {
        log.info("Running tests on device: {}", deviceName);
    }

    @AfterEach
    public void tearDown(){
        driver.quit();
    }

    @Test
    void searchContact(){
        searchScreen.setSearch("Sara");
        assertEquals("Sara Alston",searchScreen.getFirstSearchResultText());

        searchScreen.tapFirstSearchResult();
        assertAll("Address Details"
                , () -> assertEquals("Sara Alston", searchScreen.getDetailName())
                , () -> assertEquals("+1(343)-4779854", searchScreen.getPhoneNumber())
                , () -> assertEquals("eqalston16@yopmail.com", searchScreen.getEmail())
                , () -> assertEquals("311 V Street", searchScreen.getStreet1())
                , () -> assertEquals("78326 New York", searchScreen.getStreet2())
        );
    }

    @Test
    void searchContactTestRepeated(){
        searchScreen.setSearch("Sara");
        assertEquals("Sara Alston",searchScreen.getFirstSearchResultText());

        searchScreen.tapFirstSearchResult();
        assertAll("Address Details"
                , () -> assertEquals("Sara Alston", searchScreen.getDetailName())
                , () -> assertEquals("+1(343)-4779854", searchScreen.getPhoneNumber())
                , () -> assertEquals("eqalston16@yopmail.com", searchScreen.getEmail())
                , () -> assertEquals("311 V Street", searchScreen.getStreet1())
                , () -> assertEquals("78326 New York", searchScreen.getStreet2())
        );
    }
}
