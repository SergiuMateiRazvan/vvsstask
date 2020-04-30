package serenity.tests;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import serenity.helpers.Constants;
import serenity.helpers.DataReader;
import serenity.model.User;
import serenity.steps.serenity.FilterSteps;
import serenity.steps.serenity.LoginSteps;
import serenity.steps.serenity.NavigationSteps;
;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SerenityRunner.class)
public class AddFavorite {

    @Managed(uniqueSession = true)
    public WebDriver webdriver;
    private int priceFrom;
    private int priceTo;

    @Steps
    public NavigationSteps navigationSteps;
    @Steps
    public LoginSteps loginSteps;

    @Steps
    public FilterSteps filterSteps;

    @Test
    public void addToFavorites() {
        navigationSteps.navigateToHomepage();
        try {
            login();
        } catch (Exception e) {
            assert (false);
        }
        navigationSteps.navigateToHomepage();
        filterSteps.navigateToSearchPage();
        assertFalse(webdriver.getCurrentUrl().contains("audi"));
        filterSteps.completeSearchField("audi");
        assertTrue(webdriver.getCurrentUrl().contains("audi"));
//        filterSteps.completeFilterFields(1000, 2000);
    }


    private void login() throws Exception {
        User user = DataReader.readUser(Constants.userCredentialsFile);
        if (user == null) {
            throw new Exception("User not read");
        }
        navigationSteps.navigateToHomepage();
        loginSteps.navigateToLoginPage();
        loginSteps.completeCredentials(user.getUsername(), user.getPassword());
        loginSteps.login(user.getUserNickName());
    }
}
