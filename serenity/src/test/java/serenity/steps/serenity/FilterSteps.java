package serenity.steps.serenity;

import net.thucydides.core.annotations.Step;
import serenity.pages.Autovit;
import serenity.pages.SearchPage;

import static net.serenitybdd.core.Serenity.getDriver;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FilterSteps {

    Autovit page;
    SearchPage searchPage;

    @Step
    public void navigateToSearchPage() {
        page.navigateToSearch();
        assertTrue(searchPage.isOpen());
    }

    @Step
    public void completeSearchField(String search) {
        searchPage.setSearchInput(search);
        searchPage.applySearchInput();
    }

    @Step
    public void completeFilterFields(int from, int to) {
        searchPage.completePriceFrom(from);
    }
}
