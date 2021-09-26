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

    @FindBy(id = "detail_name")
    private MobileElement detailName;

    @FindBy(id = "phonenumber")
    private MobileElement phoneNumber;

    @FindBy(id = "email")
    private MobileElement email;

    @FindBy(id = "street1")
    private MobileElement street1;

    @FindBy(id = "street2")
    private MobileElement street2;

    public void setSearch(String name) {
        searchField.sendKeys(name);
    }

    public String getFirstSearchResultText() {
        return firstSearchResultName.getText();
    }

    public void tapFirstSearchResult() {
        firstSearchResultName.click();
    }

    public String getDetailName() {
        return detailName.getText();
    }

    public String getPhoneNumber() {
        return phoneNumber.getText();
    }

    public String getEmail() {
        return email.getText();
    }

    public String getStreet1() {
        return street1.getText();
    }

    public String getStreet2() {
        return street2.getText();
    }
}
