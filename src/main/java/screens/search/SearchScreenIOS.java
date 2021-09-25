package screens.search;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Slf4j
@Data
public class SearchScreenIOS implements SearchScreen{
    public SearchScreenIOS(AppiumDriver driver) {
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @FindBy(xpath = "//XCUIElementTypeSearchField[@name=\"Search for contact\"]")
    private MobileElement searchField;

    @FindBy(xpath = "//XCUIElementTypeOther[@name=\"Search results\"]/XCUIElementTypeCell/XCUIElementTypeStaticText")
    private MobileElement firstSearchResultName;

    public void setSearch(String name) {
        searchField.sendKeys(name);
    }

    public String getFirstSearchResultText() {
        return firstSearchResultName.getText();
    }

    public void navigateToFirstSearchResult() {
        firstSearchResultName.click();
    }
}
