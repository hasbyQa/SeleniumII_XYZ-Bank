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

// Withdraw form — enter amount and submit
public class WithdrawPage {
    private static final Logger logger = LoggerFactory.getLogger(WithdrawPage.class);
    private final WebDriverWait wait;

    // Same ng-model as DepositPage — Angular reuses it on different views
    @FindBy(css = "[ng-model='amount']")
    private WebElement amountInput;

    @FindBy(css = "button[type='submit']")
    private WebElement withdrawBtn;

    @FindBy(css = "[ng-show='message']")
    private WebElement message;

    public WithdrawPage(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
        logger.info("WithdrawPage initialized");
    }

    @Step("Withdraw amount: {0}")
    public void withdraw(String amount) {
        wait.until(ExpectedConditions.visibilityOf(amountInput));
        amountInput.clear();
        amountInput.sendKeys(amount);
        withdrawBtn.click();
        logger.info("Withdrew: {}", amount);
    }

    @Step("Get withdrawal result message")
    public String getMessage() {
        wait.until(ExpectedConditions.visibilityOf(message));
        return message.getText();
    }
}