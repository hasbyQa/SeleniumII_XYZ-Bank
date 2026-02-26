package com.hasby.xyzbank.pages.common;

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

// Landing page — choose between Customer Login or Bank Manager Login
public class HomePage {

    private static final Logger logger = LoggerFactory.getLogger(HomePage.class);
    private final WebDriverWait wait;

    //Routes to customer login
    @FindBy(css = "[ng-click='customer()']")
    private WebElement customerLoginBtn;

    //Routes to manager dashboard
    @FindBy(css = "[ng-click='manager()']")
    private WebElement managerLoginBtn;

    public HomePage(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
        logger.info("HomePage initialized");
    }

    @Step("Click Customer Login")
    public void clickCustomerLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(customerLoginBtn));
        customerLoginBtn.click();
        logger.info("Clicked Customer Login");
    }

    @Step("Click Bank Manager Login")
    public void clickManagerLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(managerLoginBtn));
        managerLoginBtn.click();
        logger.info("Clicked Bank Manager Login");
    }
}