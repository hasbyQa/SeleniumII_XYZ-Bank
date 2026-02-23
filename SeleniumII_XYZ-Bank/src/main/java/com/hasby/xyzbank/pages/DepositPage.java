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

// Deposit form — enter amount and click Deposit
public class DepositPage {
    private static final Logger logger = LoggerFactory.getLogger(DepositPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Amount input field
    @FindBy(css = "input[ng-model='amount']")
    private WebElement amountInput;

    // "Deposit" submit button
    @FindBy(css = "button[type='submit']")
    private WebElement depositBtn;

    // Success/error message after deposit
    @FindBy(css = "span[ng-show='message']")
    private WebElement message;

    public DepositPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
        logger.info("DepositPage initialized");
    }

    // Enter amount and click Deposit
    @Step("Deposit amount: {amount}")
    public void deposit(String amount) {
        amountInput.clear();
        amountInput.sendKeys(amount);
        depositBtn.click();
        logger.info("Deposited: {}", amount);
    }

    // Get the result message — "Deposit Successful" or error
    @Step("Get deposit message")
    public String getMessage() {
        wait.until(ExpectedConditions.visibilityOf(message));
        return message.getText();
    }
}
