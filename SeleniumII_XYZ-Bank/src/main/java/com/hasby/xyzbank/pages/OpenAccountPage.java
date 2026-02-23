package com.hasby.xyzbank.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Open Account form — select customer + currency, click Process
public class OpenAccountPage {
    private static final Logger logger = LoggerFactory.getLogger(OpenAccountPage.class);
    private final WebDriver driver;

    // id="userSelect" — one of the few elements that HAS an id in this app
    @FindBy(id = "userSelect")
    private WebElement customerSelect;

    // id="currency" — also has a real id
    @FindBy(id = "currency")
    private WebElement currencySelect;

    // Submit button for this form
    @FindBy(css = "button[type='submit']")
    private WebElement processBtn;

    public OpenAccountPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        logger.info("OpenAccountPage initialized");
    }

    // Select wraps a <select> element — lets us choose options by visible text
    @Step("Select customer: {customerName}")
    public void selectCustomer(String customerName) {
        new Select(customerSelect).selectByVisibleText(customerName);
        logger.info("Selected customer: {}", customerName);
    }

    @Step("Select currency: {currency}")
    public void selectCurrency(String currency) {
        new Select(currencySelect).selectByVisibleText(currency);
        logger.info("Selected currency: {}", currency);
    }

    @Step("Open account for {customerName} with {currency}")
    public void openAccount(String customerName, String currency) {
        selectCustomer(customerName);
        selectCurrency(currency);
        processBtn.click();
        logger.info("Account created for {} with {}", customerName, currency);
    }
}