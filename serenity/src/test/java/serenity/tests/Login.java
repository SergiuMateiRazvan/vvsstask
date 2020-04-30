package serenity.tests;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.*;
import net.thucydides.junit.annotations.Qualifier;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import serenity.helpers.DataReader;
import serenity.model.User;
import serenity.steps.serenity.LoginSteps;
import serenity.steps.serenity.NavigationSteps;


@RunWith(SerenityRunner.class)
public class Login {

    private static final String dataFile = "src/test/resources/credentials.json";

    @Managed(uniqueSession = true)
    public WebDriver webdriver;


    private User user;

    @Before
    public void readUser() throws Exception {
        user = DataReader.readUser(dataFile);
        if(user == null){
            throw new Exception("User not read");
        }
    }


    public User getUser() {
        return user;
    }

    @Steps
    public NavigationSteps navigationSteps;

    @Steps
    public LoginSteps loginSteps;

    @Test
    public void login() {
        navigationSteps.navigateToHompage();
        loginSteps.navigateToLoginPage();
        loginSteps.completeCredentials(user.getUsername(), user.getPassword());
        loginSteps.login(user.getUserNickName());
    }

    @Test
    public void loginFail() {
        navigationSteps.navigateToHompage();
        loginSteps.navigateToLoginPage();
        loginSteps.completeCredentials(user.getUsername(), user.getWrongPassword());
        loginSteps.loginFail();
    }


}
