package serenity.pages;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.DefaultUrl;
import net.thucydides.core.pages.PageObject;
import serenity.helpers.Constants;

@DefaultUrl(Constants.url)
public class Autovit extends PageObject {

    @FindBy(id = "topLoginLink")
    private WebElementFacade loginBtn;

    @FindBy(css = ".search-area__button-submit")
    private WebElementFacade searchBtn;

    @FindBy(className = "agree-button")
    private WebElementFacade agreeBtn;

    public void goToLoginPage() {
        loginBtn.click();
    }

    public Boolean isUserNameInTopBar(String username) {
        return loginBtn.waitUntilClickable().containsText(username);
    }


    public void navigateToSearch() {
        searchBtn.click();
    }

    public void agree() {
        if (agreeBtn.isVisible()) {
            agreeBtn.click();
        }
    }
}
