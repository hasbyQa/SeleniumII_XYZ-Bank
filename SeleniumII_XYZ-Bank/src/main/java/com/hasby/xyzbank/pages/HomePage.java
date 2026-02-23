package com.hasby.xyzbank.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Landing page — two buttons: Customer Login and Bank Manager Login
public class HomePage {
    private static final Logger logger = LoggerFactory.getLogger(HomePage.class);
    private final WebDriver driver;

    // No id available — ng-click is Angular's action binding, unique and stable
    @FindBy(css = "[ng-click='customer()']")
    private WebElement customerLoginBtn;

    // No id available — ng-click uniquely identifies this button's action
    @FindBy(css = "[ng-click='manager()']")
    private WebElement managerLoginBtn;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        logger.info("HomePage initialized");
    }

    @Step("Click Customer Login")
    public void clickCustomerLogin() {
        customerLoginBtn.click();
        logger.info("Clicked Customer Login");
    }

    @Step("Click Bank Manager Login")
    public void clickManagerLogin() {
        managerLoginBtn.click();
        logger.info("Clicked Bank Manager Login");
    }
}