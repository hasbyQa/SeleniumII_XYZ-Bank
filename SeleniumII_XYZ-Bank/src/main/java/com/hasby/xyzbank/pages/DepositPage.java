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

// Deposit form — enter amount and submit
public class DepositPage {
    private static final Logger logger = LoggerFactory.getLogger(DepositPage.class);
    private final WebDriverWait wait;

    @FindBy(css = "[ng-model='amount']")
    private WebElement amountInput;

    @FindBy(css = "button[type='submit']")
    private WebElement depositBtn;

    // Success/error message shown after deposit attempt
    @FindBy(css = "[ng-show='message']")
    private WebElement message;

    public DepositPage(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
        logger.info("DepositPage initialized");
    }

    @Step("Deposit amount: {0}")
    public void deposit(String amount) {
        wait.until(ExpectedConditions.visibilityOf(amountInput));
        amountInput.clear();
        amountInput.sendKeys(amount);
        depositBtn.click();
        logger.info("Deposited: {}", amount);
    }

    @Step("Get deposit result message")
    public String getMessage() {
        wait.until(ExpectedConditions.visibilityOf(message));
        return message.getText();
    }
}