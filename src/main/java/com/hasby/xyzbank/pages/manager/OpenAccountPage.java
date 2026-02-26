package com.hasby.xyzbank.pages.manager;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

// Open Account form — select customer and currency from dropdowns
public class OpenAccountPage {

    private static final Logger logger = LoggerFactory.getLogger(OpenAccountPage.class);
    private final WebDriverWait wait;

    // These two dropdowns have actual id attributes — rare in this app
    @FindBy(id = "userSelect")
    private WebElement customerDropdown;

    @FindBy(id = "currency")
    private WebElement currencyDropdown;

    @FindBy(css = "button[type='submit']")
    private WebElement processBtn;

    public OpenAccountPage(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
        logger.info("OpenAccountPage initialized");
    }

    @Step("Open account for {0} with currency {1}")
    public void openAccount(String customerName, String currency) {
        // Wait for dropdown to be populated by Angular
        wait.until(ExpectedConditions.visibilityOf(customerDropdown));
        new Select(customerDropdown).selectByVisibleText(customerName);
        new Select(currencyDropdown).selectByVisibleText(currency);
        processBtn.click();
        logger.info("Opened account for {} with {}", customerName, currency);
    }
}