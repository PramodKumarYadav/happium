package org.saucedemo.screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.support.PageFactory;
import org.saucedemo.commands.ScreenActions;

@Slf4j
@Data
public class LoginScreen {
    ScreenActions screenActions;

    public LoginScreen(AppiumDriver driver) {
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
        screenActions = new ScreenActions(driver);
    }

    @AndroidFindBy(accessibility = "test-Username")
    @iOSXCUITFindBy(accessibility = "test-Username")
    private MobileElement userNameField;

    @AndroidFindBy(accessibility = "test-Password")
    @iOSXCUITFindBy(accessibility = "test-Password")
    private MobileElement passwordField;

    @AndroidFindBy(accessibility = "test-LOGIN")
    @iOSXCUITFindBy(accessibility = "test-LOGIN")
    private MobileElement loginButton;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc='test-Error message']/android.widget.TextView")
    @iOSXCUITFindBy(accessibility = "Sorry, this user has been locked out.")
    private MobileElement errorMessage;

    private void setUserName(String userName) {
        screenActions.setTextField(userNameField, userName);
    }

    private void setPassword(String password) {
        screenActions.setTextField(passwordField, password);
    }

    private void tapLogin() {
        screenActions.clickButton(loginButton);
    }
  
    public void login(String userName, String password){
        setUserName(userName);
        setPassword(password);
        tapLogin();
    }

    public String getErrorMessage() {
        return errorMessage.getText();
    }
}
