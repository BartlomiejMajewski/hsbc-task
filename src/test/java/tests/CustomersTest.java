package tests;

import config.DriverInit;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.Search;
import pageobjects.Table;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;

public class CustomersTest {

    private static WebDriver driver;
    private static WebDriverWait wait;

    @BeforeAll
    static void initializeDriver() throws MalformedURLException {
        Path uiFilePath = Paths.get("customersPage/ui/index.html");
        driver = DriverInit.initializeWebDriver();
        wait = new WebDriverWait(driver, 30);
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        driver.get(uiFilePath.toUri().toString());
    }

    @BeforeEach
    public void refreshPage() {
        driver.navigate().refresh();
    }

    @AfterAll
    static void stopWebDriver() {
        driver.quit();
    }

    private void checkTableDefaultData(Table table) {
        List<String> expectedRow1Values = asList("1", "Alabaster", "office@alabaster.com", "Melbourne");
        List<String> expectedRow2Values = asList("2", "Postimex", "conatact@postimex.pl", "Carthage");
        List<String> expectedRow3Values = asList("3", "Bondir", "info@bond.ir", "Belfast");
        List<List<String>> expectedTableValues = asList(expectedRow1Values, expectedRow2Values, expectedRow3Values);

        assertIterableEquals(expectedTableValues, table.getTableValues(), "Expected default table values are invalid.");
    }

    private void checkEmptyTableForSearchInput(Table table, Search search, String searchInput) {
        search.clearSearchInput();
        search.sendToSearchInput(searchInput);
        assertIterableEquals(emptyList(), table.getTableValues(), "Table should be empty for search: " + searchInput);
    }

    @Test
    public void checkTableDefaultData() {
        Table table = new Table(driver, wait);

        List<String> expectedHeadersNamesList = asList("Id", "Name", "Email", "City");
        String expectedTableFooterText = "Showing 3 of 3 customers";

        wait.until(ExpectedConditions.visibilityOfElementLocated(table.getTableContainerLocator()));
        assertTrue(table.getTableContainer().isDisplayed());
        assertIterableEquals(expectedHeadersNamesList, table.getHeadersNamesList(), "Headers names list is invalid.");
        checkTableDefaultData(table);
        assertEquals(expectedTableFooterText, table.getTableFooterText(), "Table footer text is invalid.");
    }

    @Test
    public void checkSearchField() {
        Search search = new Search(driver, wait);
        Table table = new Table(driver, wait);

        List<List<String>> expectedSearchResults = singletonList(asList("1", "Alabaster", "office@alabaster.com", "Melbourne"));
        String tableFooterForAla = "Showing 1 of 3 customers filtered by term \"ala\" in Name column without match case.\nclick to clear filters";
        String tableFooterInvalidAla = "Showing 0 of 3 customers filtered by term \"ala\" in Name column with match case.\nclick to clear filters";

        wait.until(ExpectedConditions.visibilityOfElementLocated(search.getSearchContainerLocator()));
        assertFalse(search.isMatchCaseSelected(), "Match Case checkbox should not be selected by default.");
        assertEquals("", search.getSearchInputText(), "Search input should be empty.");
        assertEquals("Name", search.getColumnSelectCurrentValue(), "Column select default value not equals: Name.");
        assertFalse(search.isClearButtonDisplayed(), "Clear button should not be displayed.");

        search.sendToSearchInput("ala");
        assertIterableEquals(expectedSearchResults, table.getTableValues(), "Invalid search result for: ala");
        assertEquals(tableFooterForAla, table.getTableFooterText(), "Table footer text is invalid for ala search.");
        assertTrue(search.isClearButtonDisplayed(), "Clear button should be displayed.");

        search.clickMatchCaseCheckbox();
        assertIterableEquals(emptyList(), table.getTableValues(), "Invalid search result with match case for: ala");
        assertEquals(tableFooterInvalidAla, table.getTableFooterText(), "Table footer text is invalid for ala search with match case.");
        search.clearSearchInput();
        search.sendToSearchInput("Ala");
        assertIterableEquals(expectedSearchResults, table.getTableValues(), "Invalid search result with March Case for: Ala");

        checkEmptyTableForSearchInput(table, search, "office@alabaster.com");
        checkEmptyTableForSearchInput(table, search, "Melbourne");
        checkEmptyTableForSearchInput(table, search, "1");

        search.clickClearButton();
        checkTableDefaultData(table);
        assertEquals("", search.getSearchInputText(), "Search input should be empty.");
        assertTrue(search.isMatchCaseSelected(), "Match Case checkbox should not change after clear filter click.");
    }

    @Test
    public void checkColumnSelection() {
        Search search = new Search(driver, wait);
        Table table = new Table(driver, wait);

        List<List<String>> expectedRow1Values = singletonList(asList("1", "Alabaster", "office@alabaster.com", "Melbourne"));
        List<List<String>> expectedRow2Values = singletonList(asList("2", "Postimex", "conatact@postimex.pl", "Carthage"));
        List<List<String>> expectedRow3Values = singletonList(asList("3", "Bondir", "info@bond.ir", "Belfast"));

        search.changeColumnSelection("Id");
        assertEquals("Id", search.getColumnSelectCurrentValue(), "Column select should equals: Name.");
        search.sendToSearchInput("2");
        assertIterableEquals(expectedRow2Values, table.getTableValues(), "Invalid search result for: 2");
        checkEmptyTableForSearchInput(table, search, "6");
        checkEmptyTableForSearchInput(table, search, "Postimex");
        checkEmptyTableForSearchInput(table, search, "office@alabaster.com");
        checkEmptyTableForSearchInput(table, search, "Carthage");
        search.clearSearchInput();

        search.changeColumnSelection("Email");
        assertEquals("Email", search.getColumnSelectCurrentValue(), "Column select should equals: Email.");
        search.sendToSearchInput("bond.ir");
        assertIterableEquals(expectedRow3Values, table.getTableValues(), "Invalid search result for: bond.ir");
        checkEmptyTableForSearchInput(table, search, "bondir");
        checkEmptyTableForSearchInput(table, search, "Carthage");
        checkEmptyTableForSearchInput(table, search, "2");
        search.clearSearchInput();

        search.changeColumnSelection("City");
        assertEquals("City", search.getColumnSelectCurrentValue(), "Column select should equals: City.");
        search.sendToSearchInput("Melbourne");
        assertIterableEquals(expectedRow1Values, table.getTableValues(), "Invalid search result for: Melbourne");
        checkEmptyTableForSearchInput(table, search, "office@alabaster.com");
        checkEmptyTableForSearchInput(table, search, "Postimex");
        checkEmptyTableForSearchInput(table, search, "2");

        search.clickClearButton();
        assertEquals("City", search.getColumnSelectCurrentValue(), "Column select should not change after clear filter click.");
        checkTableDefaultData();
    }
}
