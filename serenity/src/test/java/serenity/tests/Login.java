package serenity.tests;

import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.*;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import serenity.helpers.Constants;
import serenity.steps.serenity.LoginSteps;
import serenity.steps.serenity.NavigationSteps;


@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(Constants.userCredentialsFile)
public class Login {


    @Managed(uniqueSession = true)
    public WebDriver webdriver;


    private String username;
    private String password;
    private String user;
    private String wrongPassword;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getWrongPassword() {
        return wrongPassword;
    }

    public String getUser() {
        return user;
    }

    @Steps
    public NavigationSteps navigationSteps;

    @Steps
    public LoginSteps loginSteps;

    @Test
    public void login() {
        navigationSteps.navigateToHomepage();
        loginSteps.navigateToLoginPage();
        loginSteps.completeCredentials(getUsername(), getPassword());
        loginSteps.login(getUser());
    }

    @Test
    public void loginFail() {
        navigationSteps.navigateToHomepage();
        loginSteps.navigateToLoginPage();
        loginSteps.completeCredentials(getUsername(), getWrongPassword());
        loginSteps.loginFail();
    }

}
