package com.hasby.xyzbank.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

// Customer login page — select name from dropdown and click Login
public class CustomerLoginPage {
    private static final Logger logger = LoggerFactory.getLogger(CustomerLoginPage.class);
    private final WebDriver driver;

    // "Your Name" dropdown — lists all customers with accounts
    @FindBy(id = "userSelect")
    private WebElement customerSelect;

    // Login button — only appears after selecting a customer
    @FindBy(css = "button[type='submit']")
    private WebElement loginBtn;

    public CustomerLoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        logger.info("CustomerLoginPage initialized");
    }

    @Step("Select customer: {customerName}")
    public void selectCustomer(String customerName) {
        new Select(customerSelect).selectByVisibleText(customerName);
        logger.info("Selected customer: {}", customerName);
    }

    @Step("Click Login")
    public void clickLogin() {
        loginBtn.click();
        logger.info("Clicked Login");
    }

    // Select customer and login — convenience method
    @Step("Login as customer: {customerName}")
    public void loginAs(String customerName) {
        selectCustomer(customerName);
        clickLogin();
        logger.info("Logged in as: {}", customerName);
    }

    // Get all customer names in the dropdown — used to verify a customer exists or was deleted
    @Step("Get all customer names in dropdown")
    public List<String> getCustomerNames() {
        Select select = new Select(customerSelect);
        return select.getOptions().stream()
                .map(WebElement::getText)
                .filter(text -> !text.isEmpty() && !text.equals("---Your Name---"))
                .collect(Collectors.toList());
    }

    // Check if a specific customer appears in the login dropdown
    @Step("Check if {customerName} is in dropdown")
    public boolean isCustomerInDropdown(String customerName) {
        return getCustomerNames().contains(customerName);
    }
}
