package com.hasby.xyzbank.tests;

import com.hasby.xyzbank.base.BaseTest;
import com.hasby.xyzbank.utils.AlertHelper;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@Epic("XYZ Bank Application")
@Feature("End-to-End Lifecycle")
@Owner("Hasbiyallah")
@Link(name = "XYZ Bank", url = "https://www.way2automation.com/angularjs-protractor/banking/#/login")
@DisplayName("End-to-End Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EndToEndTests extends BaseTest {

    private static final String FIRST_NAME = "E2EJohn";
    private static final String LAST_NAME = "Doe";
    private static final String FULL_NAME = FIRST_NAME + " " + LAST_NAME;
    private static final String POST_CODE = "99001";
    private static final String CURRENCY = "Dollar";
    private static final String DEPOSIT_AMOUNT = "5000";
    private static final String WITHDRAW_AMOUNT = "1500";

    @Test
    @Order(1)
    @Story("Full Customer Lifecycle")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Complete lifecycle: Manager creates customer and account → " +
            "Customer logs in, deposits, withdraws, verifies transactions → " +
            "Customer logs out → Manager confirms customer still exists.")
    @DisplayName("E2E - Full banking lifecycle")
    void testFullBankingLifecycle() {

        // ============ PHASE 1: Manager creates customer ============
        homePage.clickManagerLogin();
        managerDashboard.clickAddCustomer();
        addCustomerPage.addCustomer(FIRST_NAME, LAST_NAME, POST_CODE);

        String addAlert = AlertHelper.acceptAndGetText(driver);
        assertTrue(addAlert.contains("Customer added successfully"),
                "Step 1: Customer creation should succeed");
        logger.info("=== PHASE 1 COMPLETE: Customer created ===");

        // ============ PHASE 2: Manager opens account ============
        managerDashboard.clickOpenAccount();
        openAccountPage.openAccount(FULL_NAME, CURRENCY);

        String accountAlert = AlertHelper.acceptAndGetText(driver);
        assertTrue(accountAlert.contains("Account created successfully"),
                "Step 2: Account creation should succeed");
        logger.info("=== PHASE 2 COMPLETE: Account opened ===");

        // ============ PHASE 3: Manager verifies customer in list ============
        managerDashboard.clickCustomers();
        assertTrue(customersPage.isCustomerPresent(FIRST_NAME),
                "Step 3: Customer should appear in manager's customer list");
        logger.info("=== PHASE 3 COMPLETE: Customer verified in list ===");

        // ============ PHASE 4: Customer logs in ============
        driver.get(BASE_URL);
        initPages();
        homePage.clickCustomerLogin();
        customerLoginPage.loginAs(FULL_NAME);

        accountPage.waitForPageLoad();
        assertTrue(accountPage.getWelcomeMessage().contains(FIRST_NAME),
                "Step 4: Welcome message should contain customer name");

        int initialBalance = accountPage.getBalanceAsInt();
        assertEquals(0, initialBalance,
                "Step 4: New account should have zero balance");
        logger.info("=== PHASE 4 COMPLETE: Customer logged in, balance = {} ===", initialBalance);

        // ============ PHASE 5: Customer deposits ============
        accountPage.clickDeposit();
        depositPage.deposit(DEPOSIT_AMOUNT);

        assertEquals("Deposit Successful", depositPage.getMessage(),
                "Step 5: Deposit should show success message");

        int balanceAfterDeposit = accountPage.getBalanceAsInt();
        assertEquals(Integer.parseInt(DEPOSIT_AMOUNT), balanceAfterDeposit,
                "Step 5: Balance should equal deposit amount");
        logger.info("=== PHASE 5 COMPLETE: Deposited {}, balance = {} ===",
                DEPOSIT_AMOUNT, balanceAfterDeposit);

        // ============ PHASE 6: Customer withdraws ============
        accountPage.clickWithdraw();
        withdrawPage.withdraw(WITHDRAW_AMOUNT);

        assertEquals("Transaction successful", withdrawPage.getMessage(),
                "Step 6: Withdrawal should show success message");

        int expectedBalance = Integer.parseInt(DEPOSIT_AMOUNT) - Integer.parseInt(WITHDRAW_AMOUNT);
        int balanceAfterWithdraw = accountPage.getBalanceAsInt();
        assertEquals(expectedBalance, balanceAfterWithdraw,
                "Step 6: Balance should be deposit minus withdrawal");
        logger.info("=== PHASE 6 COMPLETE: Withdrew {}, balance = {} ===",
                WITHDRAW_AMOUNT, balanceAfterWithdraw);

        // ============ PHASE 7: Customer verifies transactions ============
        accountPage.clickTransactions();
        int txCount = transactionsPage.waitForAtLeastTransactions(2);
        assertTrue(txCount >= 2,
                "Step 7: Should have at least 2 transactions");

        assertTrue(transactionsPage.hasTransactionOfType("Credit"),
                "Step 7: Deposit should appear as Credit");
        assertTrue(transactionsPage.hasTransactionWithAmount(DEPOSIT_AMOUNT),
                "Step 7: Should show deposit amount");

        assertTrue(transactionsPage.hasTransactionOfType("Debit"),
                "Step 7: Withdrawal should appear as Debit");
        assertTrue(transactionsPage.hasTransactionWithAmount(WITHDRAW_AMOUNT),
                "Step 7: Should show withdrawal amount");
        logger.info("=== PHASE 7 COMPLETE: {} transactions verified ===", txCount);

        // ============ PHASE 8: Customer logs out ============
        transactionsPage.clickBack();

        assertEquals(expectedBalance, accountPage.getBalanceAsInt(),
                "Step 8: Balance should persist before logout");

        driver.get(BASE_URL);
        initPages();
        logger.info("=== PHASE 8 COMPLETE: Customer logged out ===");

        // ============ PHASE 9: Manager confirms customer still exists ============
        homePage.clickManagerLogin();
        managerDashboard.clickCustomers();

        assertTrue(customersPage.isCustomerPresent(FIRST_NAME),
                "Step 9: Customer should still exist after banking operations");
        logger.info("=== PHASE 9 COMPLETE: Customer still exists ===");

        logger.info("========== E2E TEST PASSED ==========");
    }
}