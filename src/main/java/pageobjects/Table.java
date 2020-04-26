package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

import static org.openqa.selenium.By.*;

public class Table {

    private static final String TABLE_CONTAINER_CLASS = ".table";
    private static final String TABLE_HEADERS_CSS = "tr th";
    private static final String TABLE_BODY_TAG = "tbody";
    private static final String TABLE_ROW_TAG = "tr";
    private static final String TABLE_CELL_TAG = "td";
    private static final String TABLE_RESUME_ID = "table-resume";
    private static final String TABLE_SLOGAN_ID = "search-slogan";

    WebDriver driver;
    WebDriverWait wait;

    public Table(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public By getTableContainerLocator() {
        return cssSelector(TABLE_CONTAINER_CLASS);
    }

    public WebElement getTableContainer() {
        return driver.findElement(getTableContainerLocator());
    }

    public List<String> getHeadersNamesList() {
        List<String> headersNames = new ArrayList<>();
        getTableContainer().findElements(cssSelector(TABLE_HEADERS_CSS)).forEach(headerElement -> headersNames.add(headerElement.getText()));
        return headersNames;
    }

    private List<WebElement> getTableRowsList() {
        return getTableContainer().findElement(tagName(TABLE_BODY_TAG)).findElements(tagName(TABLE_ROW_TAG));
    }

    public List<List<String>> getTableValues() {
        List<List<String>> rowsList = new ArrayList<>();

        for (WebElement tableRow : getTableRowsList()) {
            List<String> rowsValuesList = new ArrayList<>();
            tableRow.findElements(tagName(TABLE_CELL_TAG)).forEach(cellElement -> rowsValuesList.add(cellElement.getText()));
            rowsList.add(rowsValuesList);
        }
        return rowsList;
    }

    private WebElement getTableResumeElement() {
        return driver.findElement(id(TABLE_RESUME_ID));
    }

    private WebElement getTableSloganElement() {
        return driver.findElement(id(TABLE_SLOGAN_ID));
    }

    public String getTableFooterText() {
        String tableResumeText = getTableResumeElement().getText();
        String tableSloganText = getTableSloganElement().getText();
        if (tableSloganText.equals("")) {
            return tableResumeText;
        } else {
            return tableResumeText + " " + tableSloganText;
        }
    }

}
