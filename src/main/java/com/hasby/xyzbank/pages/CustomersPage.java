package com.hasby.xyzbank.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

// Customers list — search, view, and delete customers
public class CustomersPage {

    private static final Logger logger = LoggerFactory.getLogger(CustomersPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    //customer search filter
    @FindBy(css = "[ng-model='searchCustomer']")
    private WebElement searchInput;

    public CustomersPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
        logger.info("CustomersPage initialized");
    }

    @Step("Search for customer: {0}")
    public void searchCustomer(String name) {
        wait.until(ExpectedConditions.visibilityOf(searchInput));
        searchInput.clear();
        searchInput.sendKeys(name);
        // Angular's ng-repeat filter is client-side and instant
        // Wait for table to reflect the filter by checking DOM is stable
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table")));
    }

    @Step("Check if customer is present: {0}")
    public boolean isCustomerPresent(String name) {
        searchCustomer(name);
        List<WebElement> rows = driver.findElements(By.cssSelector("table tbody tr"));
        for (WebElement row : rows) {
            if (row.getText().contains(name)) {
                logger.info("Customer {} found", name);
                return true;
            }
        }
        logger.info("Customer {} NOT found", name);
        return false;
    }

    @Step("Delete customer: {0}")
    public void deleteCustomer(String name) {
        searchCustomer(name);
        List<WebElement> rows = driver.findElements(By.cssSelector("table tbody tr"));
        for (WebElement row : rows) {
            if (row.getText().contains(name)) {
                row.findElement(By.tagName("button")).click();
                logger.info("Deleted customer: {}", name);
                return;
            }
        }
        logger.warn("Customer {} not found for deletion", name);
    }
}