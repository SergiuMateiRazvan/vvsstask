package serenity.steps.serenity;

import net.thucydides.core.annotations.Step;
import serenity.pages.Autovit;

public class NavigationSteps {

    Autovit page;

    @Step
    public void navigateToHomepage(){
        page.open();
    }
}
