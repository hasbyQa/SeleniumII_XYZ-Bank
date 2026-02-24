package com.hasby.xyzbank.pages;

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
import java.util.List;
import java.util.stream.Collectors;

// Customer login — select name from dropdown and login
public class CustomerLoginPage {

    private static final Logger logger = LoggerFactory.getLogger(CustomerLoginPage.class);
    private final WebDriverWait wait;

    // Same id="userSelect" as OpenAccountPage — reused by Angular on different views
    @FindBy(id = "userSelect")
    private WebElement customerDropdown;

    @FindBy(css = "button[type='submit']")
    private WebElement loginBtn;

    public CustomerLoginPage(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
        logger.info("CustomerLoginPage initialized");
    }

    @Step("Login as customer: {0}")
    public void loginAs(String customerName) {
        wait.until(ExpectedConditions.visibilityOf(customerDropdown));
        new Select(customerDropdown).selectByVisibleText(customerName);
        loginBtn.click();
        logger.info("Logged in as: {}", customerName);
    }

    @Step("Get all customer names from dropdown")
    public List<String> getCustomerNames() {
        wait.until(ExpectedConditions.visibilityOf(customerDropdown));
        return new Select(customerDropdown).getOptions().stream()
                .map(WebElement::getText)
                .filter(text -> !text.isEmpty() && !text.equals("---Your Name---"))
                .collect(Collectors.toList());
    }

    @Step("Check if customer is in dropdown: {0}")
    public boolean isCustomerInDropdown(String customerName) {
        boolean found = getCustomerNames().contains(customerName);
        logger.info("Customer {} {} in dropdown", customerName, found ? "found" : "NOT found");
        return found;
    }
}