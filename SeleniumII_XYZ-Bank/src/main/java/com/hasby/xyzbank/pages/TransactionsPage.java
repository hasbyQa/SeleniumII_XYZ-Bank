package com.hasby.xyzbank.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

// Transaction history page — table of all deposits and withdrawals
public class TransactionsPage {
    private static final Logger logger = LoggerFactory.getLogger(TransactionsPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    // ng-click uniquely identifies each button's action
    @FindBy(css = "[ng-click='back()']")
    private WebElement backBtn;

    // Reset button — clears transaction history
    @FindBy(css = "[ng-click='reset()']")
    private WebElement resetBtn;

    public TransactionsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
        logger.info("TransactionsPage initialized");
    }

    // Get all rows from the transaction table
    @Step("Get all transaction rows")
    public List<WebElement> getTransactionRows() {
        return driver.findElements(By.cssSelector("table tbody tr"));
    }

    @Step("Get transaction count")
    public int getTransactionCount() {
        int count = getTransactionRows().size();
        logger.info("Transaction count: {}", count);
        return count;
    }

    // Check for "Credit" (deposit) or "Debit" (withdrawal) in transaction table
    @Step("Check for transaction type: {type}")
    public boolean hasTransactionOfType(String type) {
        for (WebElement row : getTransactionRows()) {
            if (row.getText().contains(type)) return true;
        }
        return false;
    }

    // Check if a specific amount appears in any transaction
    @Step("Check for transaction amount: {amount}")
    public boolean hasTransactionWithAmount(String amount) {
        for (WebElement row : getTransactionRows()) {
            if (row.getText().contains(amount)) return true;
        }
        return false;
    }

    // Check if Reset button is visible — used for transaction security test
    @Step("Check if Reset button is visible")
    public boolean isResetButtonVisible() {
        try {
            return resetBtn.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Click Back")
    public void clickBack() {
        backBtn.click();
        logger.info("Clicked Back");
    }
}