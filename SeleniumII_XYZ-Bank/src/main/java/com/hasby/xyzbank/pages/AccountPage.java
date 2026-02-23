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

// Customer account page — shows balance and has Transactions/Deposit/Withdrawl buttons
public class AccountPage {
    private static final Logger logger = LoggerFactory.getLogger(AccountPage.class);
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Account number display
    @FindBy(xpath = "//div[@ng-hide='noAccount']//strong[1]")
    private WebElement accountNumber;

    // Current balance display
    @FindBy(xpath = "//div[@ng-hide='noAccount']//strong[2]")
    private WebElement balance;

    // Currency display
    @FindBy(xpath = "//div[@ng-hide='noAccount']//strong[3]")
    private WebElement currency;

    // Navigation buttons
    @FindBy(css = "button[ng-click='transactions()']")
    private WebElement transactionsBtn;

    @FindBy(css = "button[ng-click='deposit()']")
    private WebElement depositBtn;

    @FindBy(css = "button[ng-click='withdrawl()']")
    private WebElement withdrawBtn;

    // Welcome message — shows customer name after login
    @FindBy(css = "span[class='fontBig ng-binding']")
    private WebElement welcomeMessage;

    public AccountPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
        logger.info("AccountPage initialized");
    }

    @Step("Get balance")
    public String getBalance() {
        String bal = balance.getText();
        logger.info("Current balance: {}", bal);
        return bal;
    }

    // Parse balance as integer for math comparisons
    @Step("Get balance as number")
    public int getBalanceAsInt() {
        return Integer.parseInt(balance.getText());
    }

    @Step("Get account number")
    public String getAccountNumber() {
        return accountNumber.getText();
    }

    @Step("Get welcome message")
    public String getWelcomeMessage() {
        return welcomeMessage.getText();
    }

    @Step("Click Transactions")
    public void clickTransactions() {
        transactionsBtn.click();
        logger.info("Clicked Transactions");
    }

    @Step("Click Deposit")
    public void clickDeposit() {
        depositBtn.click();
        logger.info("Clicked Deposit");
    }

    @Step("Click Withdraw")
    public void clickWithdraw() {
        withdrawBtn.click();
        logger.info("Clicked Withdraw");
    }

    // Wait until the welcome message appears — confirms login was successful
    @Step("Wait for account page to load")
    public void waitForPageLoad() {
        wait.until(ExpectedConditions.visibilityOf(welcomeMessage));
        logger.info("Account page loaded");
    }
}