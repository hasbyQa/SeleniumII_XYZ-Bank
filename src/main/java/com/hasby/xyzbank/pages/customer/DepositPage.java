package com.hasby.xyzbank.pages.customer;

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

// Deposit form — enter amount and submit
public class DepositPage {

    private static final Logger logger = LoggerFactory.getLogger(DepositPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Targets only the form's submit button — not nav buttons outside <form>
    private static final By FORM_SUBMIT = By.cssSelector("form button[type='submit']");

    //deposit amount
    @FindBy(css = "[ng-model='amount']")
    private WebElement amountInput;

    @FindBy(css = "[ng-show='message']")
    private WebElement message;

    public DepositPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
        logger.info("DepositPage initialized");
    }

    @Step("Deposit amount: {0}")
    public void deposit(String amount) {
        // Wait for deposit form to render — button text must be "Deposit"
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                FORM_SUBMIT, "Deposit"));

        amountInput.clear();
        amountInput.sendKeys(amount);
        driver.findElement(FORM_SUBMIT).click();
        logger.info("Deposited: {}", amount);
    }

    @Step("Get deposit result message")
    public String getMessage() {
        wait.until(ExpectedConditions.visibilityOf(message));
        return message.getText();
    }
}