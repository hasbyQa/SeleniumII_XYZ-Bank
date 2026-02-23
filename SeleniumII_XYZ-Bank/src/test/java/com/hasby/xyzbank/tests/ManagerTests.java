package com.hasby.xyzbank.tests;

import com.hasby.xyzbank.base.BaseTest;
import com.hasby.xyzbank.pages.*;
import com.hasby.xyzbank.utils.TestDataProvider;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;

import static org.junit.jupiter.api.Assertions.*;

@Epic("XYZ Bank Application")
@Feature("Bank Manager Operations")
@Owner("Hasbiyallah")
@Link(name = "XYZ Bank", url = "https://www.globalsqa.com/angularJs-protractor/BankingProject/#/login")
@DisplayName("Bank Manager Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ManagerTests extends BaseTest {

    private HomePage homePage;
    private ManagerDashboardPage managerDashboard;
    private AddCustomerPage addCustomerPage;
    private OpenAccountPage openAccountPage;
    private CustomersPage customersPage;

    private static final String FIRST_NAME = "TestJohn";
    private static final String LAST_NAME = "TestDoe";
    private static final String POST_CODE = "E725JB";
    private static final String CURRENCY = "Dollar";

    // Navigates to manager dashboard — reused by every test
    private void goToManagerDashboard() {
        homePage = new HomePage(driver);
        homePage.clickManagerLogin();
        managerDashboard = new ManagerDashboardPage(driver);
        logger.info("Navigated to Manager Dashboard");
    }

    // Handles JS alert popup — XYZ Bank uses alerts for all confirmations
    private String acceptAlertAndGetText() {
        try {
            Alert alert = driver.switchTo().alert();
            String text = alert.getText();
            alert.accept();
            logger.info("Alert accepted: {}", text);
            return text;
        } catch (NoAlertPresentException e) {
            logger.warn("No alert present");
            return "";
        }
    }

    // ==================== POSITIVE TESTS ====================

    @Test
    @Order(1)
    @Story("Add Customer")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that a bank manager can add a new customer with valid data")
    @DisplayName("M1 - Add customer with valid data")
    void testAddCustomerWithValidData() {
        goToManagerDashboard();
        managerDashboard.clickAddCustomer();

        addCustomerPage = new AddCustomerPage(driver);
        addCustomerPage.addCustomer(FIRST_NAME, LAST_NAME, POST_CODE);

        // App shows JS alert: "Customer added successfully with customer id :6"
        String alertText = acceptAlertAndGetText();
        assertTrue(alertText.contains("Customer added successfully"),
                "Alert should confirm customer was added");
    }

    // @MethodSource points to TestDataProvider.customerData() which reads customers.json
    // Each JSON object becomes one test run: {firstName, lastName, postCode}
    @ParameterizedTest(name = "M1b - Add customer: {0} {1}")
    @Order(2)
    @Story("Add Customer")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify multiple customers can be added with different valid data from JSON")
    @MethodSource("com.hasby.bank.utils.TestDataProvider#customerData")
    void testAddMultipleCustomers(String firstName, String lastName, String postCode) {
        goToManagerDashboard();
        managerDashboard.clickAddCustomer();

        addCustomerPage = new AddCustomerPage(driver);
        addCustomerPage.addCustomer(firstName, lastName, postCode);

        String alertText = acceptAlertAndGetText();
        assertTrue(alertText.contains("Customer added successfully"),
                "Alert should confirm customer " + firstName + " was added");
    }

    @Test
    @Order(3)
    @Story("Open Account")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that a bank manager can open an account for an existing customer")
    @DisplayName("M5 - Open account for existing customer")
    void testOpenAccountForCustomer() {
        // Add customer first — each test is self-contained
        goToManagerDashboard();
        managerDashboard.clickAddCustomer();
        addCustomerPage = new AddCustomerPage(driver);
        addCustomerPage.addCustomer(FIRST_NAME, LAST_NAME, POST_CODE);
        acceptAlertAndGetText();

        // Open account for that customer
        managerDashboard.clickOpenAccount();
        openAccountPage = new OpenAccountPage(driver);
        openAccountPage.openAccount(FIRST_NAME + " " + LAST_NAME, CURRENCY);

        String alertText = acceptAlertAndGetText();
        assertTrue(alertText.contains("Account created successfully"),
                "Alert should confirm account was created");
    }

    @Test
    @Order(4)
    @Story("View Customers")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that an added customer appears in the customers list")
    @DisplayName("M4b - Customer appears in customers list")
    void testCustomerAppearsInList() {
        goToManagerDashboard();
        managerDashboard.clickAddCustomer();
        addCustomerPage = new AddCustomerPage(driver);
        addCustomerPage.addCustomer(FIRST_NAME, LAST_NAME, POST_CODE);
        acceptAlertAndGetText();

        // Go to Customers tab and verify
        managerDashboard.clickCustomers();
        customersPage = new CustomersPage(driver);

        assertTrue(customersPage.isCustomerPresent(FIRST_NAME),
                "Customer " + FIRST_NAME + " should be in the list");
    }

    @Test
    @Order(5)
    @Story("Delete Customer")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that a bank manager can delete a customer from the list")
    @DisplayName("M7 - Delete customer")
    void testDeleteCustomer() {
        goToManagerDashboard();
        managerDashboard.clickAddCustomer();
        addCustomerPage = new AddCustomerPage(driver);
        addCustomerPage.addCustomer(FIRST_NAME, LAST_NAME, POST_CODE);
        acceptAlertAndGetText();

        managerDashboard.clickCustomers();
        customersPage = new CustomersPage(driver);
        customersPage.deleteCustomer(FIRST_NAME);

        // Customer should be gone from the list
        assertFalse(customersPage.isCustomerPresent(FIRST_NAME),
                "Customer " + FIRST_NAME + " should be deleted from the list");
    }

    @Test
    @Order(6)
    @Story("Delete Customer")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that a deleted customer no longer appears in the customer login dropdown")
    @DisplayName("M8 - Deleted customer can't login")
    void testDeletedCustomerNotInLoginDropdown() {
        goToManagerDashboard();
        managerDashboard.clickAddCustomer();
        addCustomerPage = new AddCustomerPage(driver);
        addCustomerPage.addCustomer(FIRST_NAME, LAST_NAME, POST_CODE);
        acceptAlertAndGetText();

        // Delete the customer
        managerDashboard.clickCustomers();
        customersPage = new CustomersPage(driver);
        customersPage.deleteCustomer(FIRST_NAME);

        // Go to customer login and verify they're gone from dropdown
        driver.get(BASE_URL);
        homePage = new HomePage(driver);
        homePage.clickCustomerLogin();

        CustomerLoginPage loginPage = new CustomerLoginPage(driver);
        assertFalse(loginPage.isCustomerInDropdown(FIRST_NAME + " " + LAST_NAME),
                "Deleted customer should not appear in login dropdown");
    }

    // ==================== NEGATIVE / VALIDATION TESTS ====================

    @Test
    @Order(7)
    @Story("Input Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("BUG-001 | Customer name field accepts numbers. " +
            "Expected: Only alphabetic characters allowed. " +
            "Actual: 'John123' is accepted without validation error. " +
            "Steps: Manager → Add Customer → enter 'John123' as first name → submit. " +
            "Severity: Normal | Priority: Medium | Fix: Add regex validation for alphabetic-only names")
    @DisplayName("M2 - Name should reject numbers")
    @Disabled("Known bug — app accepts numbers in name field without validation")
    void testNameRejectsNumbers() {
        goToManagerDashboard();
        managerDashboard.clickAddCustomer();

        addCustomerPage = new AddCustomerPage(driver);
        addCustomerPage.addCustomer("John123", LAST_NAME, POST_CODE);

        String alertText = acceptAlertAndGetText();
        assertFalse(alertText.contains("Customer added successfully"),
                "Name with numbers should be rejected");
    }

    @Test
    @Order(8)
    @Story("Input Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("BUG-002 | Customer name field accepts special characters. " +
            "Expected: Only alphabetic characters allowed per acceptance criteria. " +
            "Actual: 'John@#$' is accepted without validation error. " +
            "Steps: Manager → Add Customer → enter 'John@#$' as first name → submit. " +
            "Severity: Normal | Priority: Medium | Fix: Add regex validation ^[a-zA-Z]+$")
    @DisplayName("M3 - Name should reject special characters")
    @Disabled("Known bug — app accepts special characters in name field")
    void testNameRejectsSpecialChars() {
        goToManagerDashboard();
        managerDashboard.clickAddCustomer();

        addCustomerPage = new AddCustomerPage(driver);
        addCustomerPage.addCustomer("John@#$", LAST_NAME, POST_CODE);

        String alertText = acceptAlertAndGetText();
        assertFalse(alertText.contains("Customer added successfully"),
                "Name with special characters should be rejected");
    }

    @Test
    @Order(9)
    @Story("Input Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("BUG-003 | Postal code field accepts non-numeric input. " +
            "Expected: Only numeric characters allowed per acceptance criteria. " +
            "Actual: 'ABCDEF' is accepted without validation error. " +
            "Steps: Manager → Add Customer → enter valid names → enter 'ABCDEF' as post code → submit. " +
            "Severity: Normal | Priority: Medium | Fix: Add numeric-only validation for postal code")
    @DisplayName("M4 - Postal code should reject non-numeric")
    @Disabled("Known bug — app accepts non-numeric postal codes")
    void testPostalCodeRejectsNonNumeric() {
        goToManagerDashboard();
        managerDashboard.clickAddCustomer();

        addCustomerPage = new AddCustomerPage(driver);
        addCustomerPage.addCustomer(FIRST_NAME, LAST_NAME, "ABCDEF");

        String alertText = acceptAlertAndGetText();
        assertFalse(alertText.contains("Customer added successfully"),
                "Non-numeric postal code should be rejected");
    }
}