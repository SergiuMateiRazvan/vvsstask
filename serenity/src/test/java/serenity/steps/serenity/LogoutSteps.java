package serenity.steps.serenity;

import net.thucydides.core.annotations.Step;
import serenity.helpers.Constants;
import serenity.pages.Autovit;
import serenity.pages.ProductPage;

import static org.junit.Assert.assertTrue;

public class LogoutSteps {

    ProductPage page;
    Autovit autovitPage;

    @Step
    public void logout() {
        page.hoverAccount();
        assertTrue(page.isLoginBoxVisible());
        page.clickLogout();
        assertTrue(autovitPage.isUserNameInTopBar(Constants.unloggedName));
    }
}
