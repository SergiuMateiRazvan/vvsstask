package serenity.pages;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.DefaultUrl;
import serenity.helpers.Constants;

@DefaultUrl(Constants.accountUrl)
public class MyAccountPage extends PageObject {
    @FindBy(css = "#topLoginLink span")
    private WebElementFacade accountText;

    public boolean isLoggedIn(String username) {
        waitABit(5000);
        return accountText.getText().contains(username);
    }
}
