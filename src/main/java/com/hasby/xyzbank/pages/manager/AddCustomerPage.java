package com.hasby.xyzbank.pages.manager;

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

// Add Customer form — first name, last name, post code
public class AddCustomerPage {

    private static final Logger logger = LoggerFactory.getLogger(AddCustomerPage.class);
    private final WebDriverWait wait;

    // ng-model locators — shows what data it binds to
    @FindBy(css = "[ng-model='fName']")
    private WebElement firstNameInput;

    @FindBy(css = "[ng-model='lName']")
    private WebElement lastNameInput;

    @FindBy(css = "[ng-model='postCd']")
    private WebElement postCodeInput;

    @FindBy(css = "button[type='submit']")
    private WebElement submitBtn;

    public AddCustomerPage(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
        logger.info("AddCustomerPage initialized");
    }

    @Step("Enter first name: {0}")
    public void enterFirstName(String firstName) {
        // Wait for form to render after tab click
        wait.until(ExpectedConditions.visibilityOf(firstNameInput));
        firstNameInput.clear();
        firstNameInput.sendKeys(firstName);
    }

    @Step("Enter last name: {0}")
    public void enterLastName(String lastName) {
        lastNameInput.clear();
        lastNameInput.sendKeys(lastName);
    }

    @Step("Enter post code: {0}")
    public void enterPostCode(String postCode) {
        postCodeInput.clear();
        postCodeInput.sendKeys(postCode);
    }

    @Step("Click submit")
    public void clickSubmit() {
        submitBtn.click();
    }

    @Step("Add customer: {0} {1} {2}")
    public void addCustomer(String firstName, String lastName, String postCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostCode(postCode);
        clickSubmit();
        logger.info("Added customer: {} {} {}", firstName, lastName, postCode);
    }
}