package com.hasby.xyzbank.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

// Withdraw form — enter amount and click Withdraw
public class WithdrawPage {
    private static final Logger logger = LoggerFactory.getLogger(WithdrawPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Amount input field
    @FindBy(css = "input[ng-model='amount']")
    private WebElement amountInput;

    // "Withdraw" submit button
    @FindBy(css = "button[type='submit']")
    private WebElement withdrawBtn;

    // Success/error message after withdraw
    @FindBy(css = "span[ng-show='message']")
    private WebElement message;

    public WithdrawPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
        logger.info("WithdrawPage initialized");
    }

    // Enter amount and click Withdraw
    @Step("Withdraw amount: {amount}")
    public void withdraw(String amount) {
        amountInput.clear();
        amountInput.sendKeys(amount);
        withdrawBtn.click();
        logger.info("Withdrew: {}", amount);
    }

    // Get the result message — "Transaction successful" or error
    @Step("Get withdraw message")
    public String getMessage() {
        wait.until(ExpectedConditions.visibilityOf(message));
        return message.getText();
    }
}