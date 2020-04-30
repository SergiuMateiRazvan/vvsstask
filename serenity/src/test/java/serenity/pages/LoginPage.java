package serenity.pages;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.DefaultUrl;
import serenity.helpers.Constants;

@DefaultUrl(Constants.loginUrl)
public class LoginPage extends PageObject {

    @FindBy(id = "userEmail")
    private WebElementFacade usernameField;

    @FindBy(id = "userPass")
    private WebElementFacade passwordField;


    @FindBy(id = "se_userLogin")
    private WebElementFacade loginBtn;

    @FindBy(className = "errorbox")
    private WebElementFacade errorMessage;

    public void typeMail(String mail) {
        usernameField.type(mail);
    }

    public void typePassword(String password) {
        passwordField.type(password);
    }

    public void login() {
        loginBtn.click();
    }

    public boolean credentialsNotEmpty() {
        return !passwordField.getValue().isEmpty() && !usernameField.getValue().isEmpty();
    }

    public boolean isInvalidLogin(){
        return errorMessage.isVisible();
    }
}
