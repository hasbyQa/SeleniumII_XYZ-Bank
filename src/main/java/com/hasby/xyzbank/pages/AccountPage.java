package com.hasby.xyzbank.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class AccountPage {

    private static final Logger logger = LoggerFactory.getLogger(AccountPage.class);
    private final WebDriverWait wait;

    // XPath locator for the account info container — used by waitForAccountInfo()
    private static final By ACCOUNT_INFO_DIV =
            By.xpath("//div[@ng-hide='noAccount']");

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

    @FindBy(css = "[ng-click='withdrawl()']")
    private WebElement withdrawBtn;

    @FindBy(css = ".fontBig.ng-binding")
    private WebElement welcomeMessage;

    public AccountPage(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
        logger.info("AccountPage initialized");
    }

    // Two-layer wait: container div first, THEN individual element
    // Ensures Angular has rendered the account info section before accessing <strong> tags
    private void waitForAccountInfo() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(ACCOUNT_INFO_DIV));
    }

    @Step("Wait for account page to load")
    public void waitForPageLoad() {
        wait.until(ExpectedConditions.visibilityOf(welcomeMessage));
        logger.info("Account page loaded");
    }

    public String getWelcomeMessage() {
        wait.until(ExpectedConditions.visibilityOf(welcomeMessage));
        return welcomeMessage.getText();
    }

    @Step("Get account number")
    public String getAccountNumber() {
        waitForAccountInfo();
        wait.until(ExpectedConditions.visibilityOf(accountNumber));
        return accountNumber.getText();
    }

    @Step("Get balance")
    public int getBalanceAsInt() {
        waitForAccountInfo();
        wait.until(ExpectedConditions.visibilityOf(balance));
        return Integer.parseInt(balance.getText());
    }

    @Step("Get currency")
    public String getCurrency() {
        waitForAccountInfo();
        wait.until(ExpectedConditions.visibilityOf(currency));
        return currency.getText();
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