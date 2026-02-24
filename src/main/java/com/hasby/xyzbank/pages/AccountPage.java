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

// Account overview — shows balance, account number, and navigation buttons
public class AccountPage {

    private static final Logger logger = LoggerFactory.getLogger(AccountPage.class);
    private final WebDriverWait wait;

    // XPath required — these <strong> tags have no unique attributes, only position
    @FindBy(xpath = "//div[@ng-hide='noAccount']//strong[1]")
    private WebElement accountNumber;

    @FindBy(xpath = "//div[@ng-hide='noAccount']//strong[2]")
    private WebElement balance;

    @FindBy(xpath = "//div[@ng-hide='noAccount']//strong[3]")
    private WebElement currency;

    @FindBy(css = "[ng-click='transactions()']")
    private WebElement transactionsBtn;

    @FindBy(css = "[ng-click='deposit()']")
    private WebElement depositBtn;

    // App misspells "withdrawal" as "withdrawl"
    @FindBy(css = "[ng-click='withdrawl()']")
    private WebElement withdrawBtn;

    // Welcome message — only reliable element to confirm page loaded
    @FindBy(css = ".fontBig.ng-binding")
    private WebElement welcomeMessage;

    public AccountPage(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
        logger.info("AccountPage initialized");
    }

    // Call after login — ensures Angular has rendered the account view
    @Step("Wait for account page to load")
    public void waitForPageLoad() {
        wait.until(ExpectedConditions.visibilityOf(welcomeMessage));
        logger.info("Account page loaded");
    }

    public String getWelcomeMessage() {
        wait.until(ExpectedConditions.visibilityOf(welcomeMessage));
        return welcomeMessage.getText();
    }

    public int getBalanceAsInt() {
        wait.until(ExpectedConditions.visibilityOf(balance));
        return Integer.parseInt(balance.getText());
    }

    @Step("Click Transactions")
    public void clickTransactions() {
        wait.until(ExpectedConditions.elementToBeClickable(transactionsBtn));
        transactionsBtn.click();
        logger.info("Clicked Transactions");
    }

    @Step("Click Deposit")
    public void clickDeposit() {
        wait.until(ExpectedConditions.elementToBeClickable(depositBtn));
        depositBtn.click();
        logger.info("Clicked Deposit");
    }

    @Step("Click Withdraw")
    public void clickWithdraw() {
        wait.until(ExpectedConditions.elementToBeClickable(withdrawBtn));
        withdrawBtn.click();
        logger.info("Clicked Withdraw");
    }
}