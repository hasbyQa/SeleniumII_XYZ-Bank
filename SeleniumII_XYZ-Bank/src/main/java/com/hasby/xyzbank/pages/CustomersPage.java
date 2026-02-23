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

    // ng-model for the search/filter input — unique data binding
    @FindBy(css = "[ng-model='searchCustomer']")
    private WebElement searchInput;

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

    // Returns all visible rows in the customer table
    @Step("Get customer rows")
    public List<WebElement> getCustomerRows() {
        return driver.findElements(By.cssSelector("table tbody tr"));
    }

    // Check if a customer exists by searching and checking table rows
    @Step("Check if customer {name} exists")
    public boolean isCustomerPresent(String name) {
        searchCustomer(name);
        List<WebElement> rows = getCustomerRows();
        if (rows.isEmpty()) return false;
        return rows.get(0).getText().contains(name);
    }

    // Delete first customer matching the search
    @Step("Delete customer: {name}")
    public void deleteCustomer(String name) {
        searchCustomer(name);
        List<WebElement> rows = getCustomerRows();
        if (!rows.isEmpty()) {
            // Delete button is inside each row
            rows.get(0).findElement(By.tagName("button")).click();
            logger.info("Deleted customer: {}", name);
        }
    }

    // Get text of first row — for assertions
    @Step("Get first row text")
    public String getFirstRowText() {
        List<WebElement> rows = getCustomerRows();
        if (rows.isEmpty()) return "";
        return rows.get(0).getText();
    }
}