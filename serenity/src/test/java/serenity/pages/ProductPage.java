package serenity.pages;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.DefaultUrl;
import org.apache.pdfbox.contentstream.operator.graphics.MoveTo;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.server.handler.interactions.touch.Move;
import serenity.helpers.Constants;

@DefaultUrl(Constants.productUrl)
public class ProductPage extends PageObject {

    @FindBy(className = "detailpage")
    private WebElementFacade body;

    @FindBy(css = ".flex-container-main__right a.favorite-button")
    private WebElementFacade addToFavoritesBtn;

    @FindBy(css = ".flex-container-main__right .for-favorite")
    private WebElementFacade addToFavoritesLabel;

    @FindBy(css = ".fflex-container-main__right .for-unfavorite")
    private WebElementFacade removeFromFavoritesLabel;

    @FindBy(css = "#saveFavDiv button")
    private WebElementFacade closeModalBtn;

    @FindBy(id = "my-account-link")
    private WebElementFacade accountLink;

    @FindBy(id = "userLoginBox")
    private WebElementFacade loginBox;

    @FindBy(id="login-box-logout")
    private WebElementFacade logoutBox;


    public boolean isVisible() {
        return body.isVisible();
    }

    public void addToFavorites() {
        addToFavoritesBtn.click();
    }

    public boolean isAddToFavoritesLabelVisible() {
        return addToFavoritesLabel.isVisible();
    }

    public boolean isRemoveFromFavoritesLabelVisible() {
        closeModal();
        return addToFavoritesBtn.hasClass("is-favorite");
    }

    public void closeModal() {
        closeModalBtn.click();
    }

    public void hoverAccount() {
        Actions action = new Actions(getDriver());
        action.moveToElement(accountLink).perform();
    }

    public boolean isLoginBoxVisible() {
        return loginBox.isVisible();
    }

    public void clickLogout() {
        logoutBox.click();
        waitABit(5000);
    }

}
