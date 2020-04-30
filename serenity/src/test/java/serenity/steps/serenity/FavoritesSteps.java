package serenity.steps.serenity;

import net.thucydides.core.annotations.Step;
import serenity.pages.FavoritesPage;
import serenity.pages.ProductPage;

import static org.junit.Assert.assertTrue;

public class FavoritesSteps {

    ProductPage productPage;
    FavoritesPage favoritesPage;

    @Step
    public void addProductToFavorites() {
        assertTrue(productPage.isAddToFavoritesLabelVisible());
        productPage.addToFavorites();
        assertTrue(productPage.isRemoveFromFavoritesLabelVisible());
    }

    @Step
    public void removeFromFavorites() {
        favoritesPage.open();
        favoritesPage.removeFromFavorites();
    }
}
