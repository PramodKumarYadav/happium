package org.saucedemo.screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

@Slf4j
@Data
public class LoginScreen {
    private WebDriverWait wait;
    private static final Integer TIME_OUT_IN_FIVE_SECONDS = 5;
    ScreenActions screenActions;

    public LoginScreen(AppiumDriver driver) {
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
        wait = new WebDriverWait(driver, TIME_OUT_IN_FIVE_SECONDS);

        screenActions = new ScreenActions(driver);
    }

    @AndroidFindBy(accessibility = "test-Username")
    @iOSXCUITFindBy(accessibility = "test-Username")
    private MobileElement userNameField;

    @AndroidFindBy(accessibility = "test-Password")
    @iOSXCUITFindBy(accessibility = "test-Password")
    private MobileElement passwordField;

    @AndroidFindBy(accessibility = "test-LOGIN")
    @iOSXCUITFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-LOGIN\"]/android.widget.TextView")
    private MobileElement loginButton;

    private void setUserName(String userName) {
        screenActions.setTextField(userNameField, userName);
    }

    private void setPassword(String password) {
        screenActions.setTextField(passwordField, password);
    }

    private void tapLogin() {
        loginButton.click();
    }

    public void login(String userName, String password){
        setUserName(userName);
        setPassword(password);
        tapLogin();
    }
}
