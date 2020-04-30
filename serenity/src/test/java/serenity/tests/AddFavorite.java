package serenity.tests;

import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import serenity.helpers.Constants;
import serenity.steps.serenity.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(Constants.searchTermFile)
public class AddFavorite {

    @Managed(uniqueSession = true)
    public WebDriver webdriver;

    private String searchTerm;
    private String username;
    private String password;
    private String user;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUser() {
        return user;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    @Steps
    public NavigationSteps navigationSteps;
    @Steps
    public LoginSteps loginSteps;
    @Steps
    public FilterSteps filterSteps;
    @Steps
    public FavoritesSteps favoritesSteps;
    @Steps
    public LogoutSteps logoutSteps;


    @Test
    public void addToFavorites() throws InterruptedException {
        ensureHomepage();
        login();
        ensureNoFavorites();
        navigationSteps.navigateToHomepage();
        applyFilter(getSearchTerm());
        goOnProductPage();
        applyFavorites();
        logoutSteps.logout();
    }

    private void ensureNoFavorites() {
        favoritesSteps.removeFromFavorites();
    }

    private void applyFavorites() {
        favoritesSteps.addProductToFavorites();
    }

    private void goOnProductPage() {
        filterSteps.clickOnProduct();
    }

    private void applyFilter(String filter) throws InterruptedException {
        filterSteps.navigateToSearchPage();
        assertFalse(webdriver.getCurrentUrl().contains(filter));
        filterSteps.completeSearchField(filter);
        assertTrue(webdriver.getCurrentUrl().contains(filter));
    }

    private void ensureHomepage() {
        if (!webdriver.getCurrentUrl().equals(Constants.url)) {
            navigationSteps.navigateToHomepage();
        }
    }


    private void login() {
        navigationSteps.navigateToHomepage();
        loginSteps.navigateToLoginPage();
        loginSteps.completeCredentials(getUsername(), getPassword());
        loginSteps.login(getUser());
    }
}
