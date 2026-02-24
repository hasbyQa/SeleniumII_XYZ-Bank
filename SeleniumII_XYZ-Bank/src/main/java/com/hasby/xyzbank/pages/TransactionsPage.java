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

public class TransactionsPage {
    private static final Logger logger = LoggerFactory.getLogger(TransactionsPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(css = "[ng-click='back()']")
    private WebElement backBtn;

    @FindBy(css = "[ng-click='reset()']")
    private WebElement resetBtn;

    public TransactionsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
        logger.info("TransactionsPage initialized");
    }

    private List<WebElement> getRows() {
        return driver.findElements(By.cssSelector("table tbody tr"));
    }

    @Step("Get transaction count")
    public int getTransactionCount() {
        wait.until(ExpectedConditions.elementToBeClickable(backBtn));
        return getRows().size();
    }

    // Waits until at least minCount transactions appear in the table
    // Use this after deposit/withdraw to let Angular register the transaction
    @Step("Wait for at least {0} transactions")
    public int waitForAtLeastTransactions(int minCount) {
        // Wait for transactions page to fully load
        wait.until(ExpectedConditions.elementToBeClickable(backBtn));

        // Check if rows are already present
        if (getRows().size() >= minCount) {
            return getRows().size();
        }

        // Angular rendered before transactions were registered in memory.
        // Navigate back to account page, then return — forces controller re-init
        clickBack();
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("[ng-click='transactions()']")));
        driver.findElement(By.cssSelector("[ng-click='transactions()']")).click();

        // Wait for fresh render
        wait.until(ExpectedConditions.elementToBeClickable(backBtn));
        return getRows().size();
    }

    @Step("Check for transaction type: {0}")
    public boolean hasTransactionOfType(String type) {
        for (WebElement row : getRows()) {
            if (row.getText().contains(type)) return true;
        }
        return false;
    }

    @Step("Check for transaction amount: {0}")
    public boolean hasTransactionWithAmount(String amount) {
        for (WebElement row : getRows()) {
            if (row.getText().contains(amount)) return true;
        }
        return false;
    }

    @Step("Click Back")
    public void clickBack() {
        wait.until(ExpectedConditions.elementToBeClickable(backBtn));
        backBtn.click();
        logger.info("Clicked Back");
    }

    public boolean isResetButtonVisible() {
        return resetBtn.isDisplayed();
    }
}