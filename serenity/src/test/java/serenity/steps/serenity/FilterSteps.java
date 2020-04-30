package serenity.steps.serenity;

import net.thucydides.core.annotations.Step;
import serenity.pages.Autovit;
import serenity.pages.ProductPage;
import serenity.pages.SearchPage;

import static org.junit.Assert.assertTrue;

public class FilterSteps {

    Autovit page;
    SearchPage searchPage;
    ProductPage productPage;

    @Step
    public void navigateToSearchPage() {
        page.navigateToSearch();
        assertTrue(searchPage.isOpen());
    }

    @Step
    public void completeSearchField(String search) {
        searchPage.setSearchInput(search);
//        searchPage.applySearchInput();
    }

    @Step
    public void completeFilterFields(int from, int to) {
        searchPage.completePriceFrom(from);
    }

    @Step
    public void clickOnProduct(){
        page.agree();
        searchPage.goToArticle();
        assertTrue(productPage.isVisible());
    }
}
