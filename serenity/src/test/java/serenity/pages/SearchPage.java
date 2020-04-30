package serenity.pages;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.DefaultUrl;
import serenity.helpers.Constants;

@DefaultUrl(Constants.searchUrl)
public class SearchPage extends PageObject {


    @FindBy(id="searchbox")
    private WebElementFacade searchBox;

    @FindBy(id="search-text")
    private WebElementFacade searchInput;


    public boolean isOpen() {
        return searchBox.isVisible();
    }

    public void completePriceFrom(int from) {
    }

    public void setSearchInput(String search) {
        searchInput.type(search);
    }

    public void applySearchInput() {
        searchBox.click();
    }
}
