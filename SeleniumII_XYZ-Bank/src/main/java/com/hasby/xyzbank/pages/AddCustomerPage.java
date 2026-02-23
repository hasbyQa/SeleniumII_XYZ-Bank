package com.hasby.xyzbank.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Add Customer form — First Name, Last Name, Post Code fields
public class AddCustomerPage {
    private static final Logger logger = LoggerFactory.getLogger(AddCustomerPage.class);
    private final WebDriver driver;

    // ng-model = Angular's data binding — like an id tied to the data model, unique per field
    @FindBy(css = "[ng-model='fName']")
    private WebElement firstNameInput;

    @FindBy(css = "[ng-model='lName']")
    private WebElement lastNameInput;

    @FindBy(css = "[ng-model='postCd']")
    private WebElement postCodeInput;

    // Submit button inside the addCust form — ng-submit uniquely identifies this form
    @FindBy(css = "[ng-submit='addCust()'] button[type='submit']")
    private WebElement addCustomerSubmitBtn;

    public AddCustomerPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        logger.info("AddCustomerPage initialized");
    }

    @Step("Enter first name: {firstName}")
    public void enterFirstName(String firstName) {
        firstNameInput.clear();
        firstNameInput.sendKeys(firstName);
        logger.info("Entered first name: {}", firstName);
    }

    @Step("Enter last name: {lastName}")
    public void enterLastName(String lastName) {
        lastNameInput.clear();
        lastNameInput.sendKeys(lastName);
        logger.info("Entered last name: {}", lastName);
    }

    @Step("Enter post code: {postCode}")
    public void enterPostCode(String postCode) {
        postCodeInput.clear();
        postCodeInput.sendKeys(postCode);
        logger.info("Entered post code: {}", postCode);
    }

    // Fill all fields and submit — convenience method
    @Step("Add customer: {firstName} {lastName} with post code {postCode}")
    public void addCustomer(String firstName, String lastName, String postCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostCode(postCode);
        addCustomerSubmitBtn.click();
        logger.info("Submitted new customer: {} {} {}", firstName, lastName, postCode);
    }
}