package serenity.steps.serenity;

import net.thucydides.core.annotations.Step;
import serenity.pages.Autovit;
import serenity.pages.LoginPage;
import serenity.pages.MyAccountPage;

import static org.junit.Assert.assertTrue;

public class LoginSteps {

    Autovit page;
    LoginPage loginPage;
    MyAccountPage accountPage;
    NavigationSteps navigation;

    @Step
    public void navigateToLoginPage(){
        page.goToLoginPage();
    }

    @Step
    public void completeCredentials(String username, String password) {
        loginPage.typeMail(username);
        loginPage.typePassword(password);
        assertTrue(loginPage.credentialsNotEmpty());
    }

    @Step
    public void login(String username){
        loginPage.login();
        assertTrue(accountPage.isLoggedIn(username));
    }

    @Step
    public void loginFail() {
        loginPage.login();
        assertTrue(loginPage.isInvalidLogin());
    }
}
