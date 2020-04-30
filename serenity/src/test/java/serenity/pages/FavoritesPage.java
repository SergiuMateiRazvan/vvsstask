package serenity.pages;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.DefaultUrl;
import serenity.helpers.Constants;

@DefaultUrl(Constants.favoritesUrl)
public class FavoritesPage extends PageObject {

    @FindBy(className = "removeObservedAd")
    private WebElementFacade removeFromFavoritesBtn;

    public void removeFromFavorites() {
        if (removeFromFavoritesBtn.isVisible())
            removeFromFavoritesBtn.click();
    }
}
