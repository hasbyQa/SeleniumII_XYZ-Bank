package com.hasby.xyzbank.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Manager dashboard — three tab buttons: Add Customer, Open Account, Customers
public class ManagerDashboardPage {
    private static final Logger logger = LoggerFactory.getLogger(ManagerDashboardPage.class);
    private final WebDriver driver;

    // No id — ng-class is Angular's style binding, unique per tab
    @FindBy(css = "[ng-class='btnClass1']")
    private WebElement addCustomerBtn;

    @FindBy(css = "[ng-class='btnClass2']")
    private WebElement openAccountBtn;

    @FindBy(css = "[ng-class='btnClass3']")
    private WebElement customersBtn;

    public ManagerDashboardPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        logger.info("ManagerDashboardPage initialized");
    }

    @Step("Click Add Customer tab")
    public void clickAddCustomer() {
        addCustomerBtn.click();
        logger.info("Clicked Add Customer tab");
    }

    @Step("Click Open Account tab")
    public void clickOpenAccount() {
        openAccountBtn.click();
        logger.info("Clicked Open Account tab");
    }

    @Step("Click Customers tab")
    public void clickCustomers() {
        customersBtn.click();
        logger.info("Clicked Customers tab");
    }
}