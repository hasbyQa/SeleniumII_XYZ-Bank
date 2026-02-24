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

public class WithdrawPage {
    private static final Logger logger = LoggerFactory.getLogger(WithdrawPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    // form button[type='submit'] targets ONLY the form's submit button
    // Nav buttons (Home, Logout, etc.) are outside <form> — won't match
    private static final By FORM_SUBMIT = By.cssSelector("form button[type='submit']");

    @FindBy(css = "[ng-model='amount']")
    private WebElement amountInput;

    public WithdrawPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
        logger.info("WithdrawPage initialized");
    }

    @Step("Withdraw amount: {0}")
    public void withdraw(String amount) {
        // Angular reuses [ng-model='amount'] for both deposit and withdraw forms
        // Must wait until form button text changes from "Deposit" to "Withdraw"
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                FORM_SUBMIT, "Withdraw"));

        amountInput.clear();
        amountInput.sendKeys(amount);
        driver.findElement(FORM_SUBMIT).click();
        logger.info("Withdrew: {}", amount);
    }

    @Step("Get withdrawal result message")
    public String getMessage() {
        // Wait until message element has non-empty text (starts empty on withdraw view)
        wait.until(d -> {
            String text = d.findElement(By.cssSelector("[ng-show='message']")).getText();
            return text != null && !text.isEmpty();
        });
        return driver.findElement(By.cssSelector("[ng-show='message']")).getText();
    }
}