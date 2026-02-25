package com.hasby.xyzbank.tests;

import com.hasby.xyzbank.base.BaseTest;
import com.hasby.xyzbank.utils.AlertHelper;
import com.hasby.xyzbank.utils.TestConstants;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@Epic("XYZ Bank Application")
@Feature("Customer Banking Operations")
@Owner("Hasbiyallah")
@Link(name = "XYZ Bank", url = "https://www.way2automation.com/angularjs-protractor/banking/#/login")
@DisplayName("Customer Banking Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerTests extends BaseTest {

    // No page field declarations — inherited from BaseTest

    private void setupCustomerWithAccountAndLogin() {
        homePage.clickManagerLogin();
        managerDashboard.clickAddCustomer();
        addCustomerPage.addCustomer(
                TestConstants.CUST_FIRST_NAME,
                TestConstants.CUST_LAST_NAME,
                TestConstants.CUST_POST_CODE);
        AlertHelper.acceptAndGetText(driver);

        managerDashboard.clickOpenAccount();
        openAccountPage.openAccount(TestConstants.CUST_FULL_NAME, TestConstants.CURRENCY);
        AlertHelper.acceptAndGetText(driver);

        driver.get(BASE_URL);
        initPages();
        homePage.clickCustomerLogin();
        customerLoginPage.loginAs(TestConstants.CUST_FULL_NAME);

        accountPage.waitForPageLoad();
        logger.info("Customer {} ready", TestConstants.CUST_FULL_NAME);
    }

    private void depositAmount(String amount) {
        accountPage.clickDeposit();
        depositPage.deposit(amount);
    }

    // ==================== POSITIVE TESTS ====================

    @Test
    @Order(1)
    @Story("Customer Login")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify customer can login and see welcome message with their name")
    @DisplayName("C1 - Customer login")
    void testCustomerLogin() {
        setupCustomerWithAccountAndLogin();
        String welcome = accountPage.getWelcomeMessage();
        assertTrue(welcome.contains(TestConstants.CUST_FIRST_NAME),
                "Welcome message should contain customer name");
    }

    @Test
    @Order(2)
    @Story("View Transactions")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify new account has empty transaction history")
    @DisplayName("C2 - View transactions (empty)")
    void testViewTransactionsEmpty() {
        setupCustomerWithAccountAndLogin();
        accountPage.clickTransactions();
        assertEquals(0, transactionsPage.getTransactionCount(),
                "New account should have zero transactions");
    }

    @Test
    @Order(3)
    @Story("Deposit Funds")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify deposit increases balance by exact amount")
    @DisplayName("C3 - Deposit valid amount")
    void testDepositValidAmount() {
        setupCustomerWithAccountAndLogin();
        int balanceBefore = accountPage.getBalanceAsInt();

        depositAmount("500");

        assertEquals("Deposit Successful", depositPage.getMessage(),
                "Should show deposit success message");
        assertEquals(balanceBefore + 500, accountPage.getBalanceAsInt(),
                "Balance should increase by deposit amount");
    }

    @Test
    @Order(4)
    @Story("Withdraw Funds")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify withdrawal decreases balance by exact amount")
    @DisplayName("C5 - Withdraw valid amount")
    void testWithdrawValidAmount() {
        setupCustomerWithAccountAndLogin();
        depositAmount("1000");
        int balanceBefore = accountPage.getBalanceAsInt();

        accountPage.clickWithdraw();
        withdrawPage.withdraw("300");

        assertEquals("Transaction successful", withdrawPage.getMessage(),
                "Should show withdraw success message");
        assertEquals(balanceBefore - 300, accountPage.getBalanceAsInt(),
                "Balance should decrease by withdrawal amount");
    }

    @Test
    @Order(5)
    @Story("Transaction History")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify deposit appears as Credit in transaction history")
    @DisplayName("C8 - Transaction appears after deposit")
    void testTransactionAppearsAfterDeposit() {
        setupCustomerWithAccountAndLogin();
        depositAmount("750");

        accountPage.clickTransactions();
        transactionsPage.waitForAtLeastTransactions(1);

        assertTrue(transactionsPage.getTransactionCount() > 0,
                "Should have at least one transaction");
        assertTrue(transactionsPage.hasTransactionOfType("Credit"),
                "Deposit should appear as Credit");
        assertTrue(transactionsPage.hasTransactionWithAmount("750"),
                "Should show deposited amount");
    }

    @Test
    @Order(6)
    @Story("Transaction History")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify withdrawal appears as Debit in transaction history")
    @DisplayName("C9 - Transaction appears after withdrawal")
    void testTransactionAppearsAfterWithdrawal() {
        setupCustomerWithAccountAndLogin();
        depositAmount("1000");

        accountPage.clickWithdraw();
        withdrawPage.withdraw("400");

        assertEquals("Transaction successful", withdrawPage.getMessage(),
                "Withdrawal should succeed");

        accountPage.clickTransactions();
        transactionsPage.waitForAtLeastTransactions(2);

        assertTrue(transactionsPage.hasTransactionOfType("Debit"),
                "Withdrawal should appear as Debit");
        assertTrue(transactionsPage.hasTransactionWithAmount("400"),
                "Should show withdrawn amount");
    }

    // ==================== NEGATIVE TESTS ====================

    @Test
    @Order(7)
    @Story("Deposit Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify depositing zero does not change balance")
    @DisplayName("C4 - Deposit zero rejected")
    void testDepositZeroOrNegative() {
        setupCustomerWithAccountAndLogin();
        int balanceBefore = accountPage.getBalanceAsInt();

        depositAmount("0");

        assertEquals(balanceBefore, accountPage.getBalanceAsInt(),
                "Balance should not change after depositing zero");
    }

    @Test
    @Order(8)
    @Story("Withdraw Validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify withdrawing more than balance shows error")
    @DisplayName("C6 - Withdraw more than balance rejected")
    void testWithdrawMoreThanBalance() {
        setupCustomerWithAccountAndLogin();
        depositAmount("100");

        accountPage.clickWithdraw();
        withdrawPage.withdraw("99999");

        assertNotEquals("Transaction successful", withdrawPage.getMessage(),
                "Withdrawing more than balance should not succeed");
    }

    @Test
    @Order(9)
    @Story("Withdraw Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify withdrawing zero does not change balance")
    @DisplayName("C7 - Withdraw zero rejected")
    void testWithdrawZeroOrNegative() {
        setupCustomerWithAccountAndLogin();
        depositAmount("500");
        int balanceBefore = accountPage.getBalanceAsInt();

        accountPage.clickWithdraw();
        withdrawPage.withdraw("0");

        assertEquals(balanceBefore, accountPage.getBalanceAsInt(),
                "Balance should not change after withdrawing zero");
    }

    @Test
    @Order(10)
    @Story("Transaction Security")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify transactions persist after navigating away and back")
    @DisplayName("C10 - Transaction history persists")
    void testTransactionHistoryPersists() {
        setupCustomerWithAccountAndLogin();
        depositAmount("250");

        accountPage.clickTransactions();
        int countBefore = transactionsPage.waitForAtLeastTransactions(1);
        assertTrue(countBefore > 0, "Should have at least one transaction");

        transactionsPage.clickBack();
        accountPage.clickTransactions();

        assertEquals(countBefore, transactionsPage.waitForAtLeastTransactions(1),
                "Transactions should persist after navigating away and back");
    }
}