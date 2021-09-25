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
public class SearchScreenAndroid implements SearchScreen{
    public SearchScreenAndroid(AppiumDriver driver) {
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @FindBy(id = "main_search")
    private MobileElement searchField;

    @FindBy(id = "name")
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
