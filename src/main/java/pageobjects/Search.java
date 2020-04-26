package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.id;

public class Search {

    private static final String SEARCH_CONTAINER_CSS = ".container > div";
    private static final String SEARCH_INPUT_ID = "search-input";
    private static final String COLUMN_SELECT_ID = "search-column";
    private static final String MARCH_CASE_ID = "match-case";
    private static final String CLEAR_BUTTON_ID = "clear-button";

    WebDriver driver;
    WebDriverWait wait;

    public Search(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public By getSearchContainerLocator() {
        return cssSelector(SEARCH_CONTAINER_CSS);
    }

    private WebElement getSearchContainer() {
        return driver.findElement(getSearchContainerLocator());
    }

    private WebElement getSearchInput() {
        return getSearchContainer().findElement(id(SEARCH_INPUT_ID));
    }

    public String getSearchInputText() {
        return getSearchInput().getText();
    }

    public void sendToSearchInput(String searchInputText) {
        getSearchInput().sendKeys(searchInputText);
    }

    public void clearSearchInput() {
        getSearchInput().clear();
    }

    private Select getColumnSelect() {
        return new Select(getSearchContainer().findElement(id(COLUMN_SELECT_ID)));
    }

    public String getColumnSelectCurrentValue() {
        return getColumnSelect().getFirstSelectedOption().getText();
    }

    public void changeColumnSelection(String optionValue) {
        getColumnSelect().selectByVisibleText(optionValue);
    }

    private WebElement getMatchCaseCheckbox() {
        return getSearchContainer().findElement(id(MARCH_CASE_ID));
    }

    public boolean isMatchCaseSelected() {
        return getMatchCaseCheckbox().isSelected();
    }

    public void clickMatchCaseCheckbox() {
        getMatchCaseCheckbox().click();
    }

    private WebElement getClearButton() {
        return driver.findElement(id(CLEAR_BUTTON_ID));
    }

    public void clickClearButton() {
        getClearButton().click();
    }

    public boolean isClearButtonDisplayed() {
        return getClearButton().isDisplayed();
    }
}
