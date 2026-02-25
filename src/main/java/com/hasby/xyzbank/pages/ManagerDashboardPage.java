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

// Manager dashboard — three tab buttons for managing customers and accounts
public class ManagerDashboardPage {

    private static final Logger logger = LoggerFactory.getLogger(ManagerDashboardPage.class);
    private final WebDriverWait wait;

    // ng-click locators — describe what each button does

    //shows add customer form
    @FindBy(css = "[ng-click='addCust()']")
    private WebElement addCustomerBtn;

    //shows open account form
    @FindBy(css = "[ng-click='openAccount()']")
    private WebElement openAccountBtn;

    //shows customer list
    @FindBy(css = "[ng-click='showCust()']")
    private WebElement customersBtn;

    public ManagerDashboardPage(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
        logger.info("ManagerDashboardPage initialized");
    }

    @Step("Click Add Customer tab")
    public void clickAddCustomer() {
        wait.until(ExpectedConditions.elementToBeClickable(addCustomerBtn));
        addCustomerBtn.click();
        logger.info("Clicked Add Customer tab");
    }

    @Step("Click Open Account tab")
    public void clickOpenAccount() {
        wait.until(ExpectedConditions.elementToBeClickable(openAccountBtn));
        openAccountBtn.click();
        logger.info("Clicked Open Account tab");
    }

    @Step("Click Customers tab")
    public void clickCustomers() {
        wait.until(ExpectedConditions.elementToBeClickable(customersBtn));
        customersBtn.click();
        logger.info("Clicked Customers tab");
    }
}