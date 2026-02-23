package com.hasby.xyzbank.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

// Customers list page — search, view, and delete customers
public class CustomersPage {
    private static final Logger logger = LoggerFactory.getLogger(CustomersPage.class);
    private final WebDriver driver;

    // Search box to filter the customer table
    @FindBy(css = "input[ng-model='searchCustomer']")
    private WebElement searchInput;

    // All rows in the customer table body
    @FindBy(css = "table tbody tr")
    private WebElement firstRow;

    public CustomersPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        logger.info("CustomersPage initialized");
    }

    @Step("Search for customer: {name}")
    public void searchCustomer(String name) {
        searchInput.clear();
        searchInput.sendKeys(name);
        logger.info("Searched for customer: {}", name);
    }

    // Returns all customer rows currently visible in the table
    @Step("Get customer rows")
    public List<WebElement> getCustomerRows() {
        return driver.findElements(By.cssSelector("table tbody tr"));
    }

    // Check if a customer exists in the table by name
    @Step("Check if customer {name} exists")
    public boolean isCustomerPresent(String name) {
        searchCustomer(name);
        List<WebElement> rows = getCustomerRows();
        // If no rows or the single row has no <td> cells, customer doesn't exist
        if (rows.isEmpty()) return false;
        String rowText = rows.get(0).getText();
        return rowText.contains(name);
    }

    // Delete the first customer that appears after searching
    @Step("Delete customer: {name}")
    public void deleteCustomer(String name) {
        searchCustomer(name);
        List<WebElement> rows = getCustomerRows();
        if (!rows.isEmpty()) {
            // Delete button is the last cell in each row
            WebElement deleteBtn = rows.get(0).findElement(By.tagName("button"));
            deleteBtn.click();
            logger.info("Deleted customer: {}", name);
        }
    }

    // Get the text content of the first matching row (for assertions)
    @Step("Get first row text")
    public String getFirstRowText() {
        List<WebElement> rows = getCustomerRows();
        if (rows.isEmpty()) return "";
        return rows.get(0).getText();
    }
}