package com.hasby.xyzbank.tests;

import com.hasby.xyzbank.base.BaseTest;
import com.hasby.xyzbank.utils.AlertHelper;
import com.hasby.xyzbank.utils.TestConstants;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

@Epic("XYZ Bank Application")
@Feature("Bank Manager Operations")
@Owner("Hasbiyallah")
@Link(name = "XYZ Bank", url = "https://www.way2automation.com/angularjs-protractor/banking/#/login")
@DisplayName("Bank Manager Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ManagerTests extends BaseTest {

    // No page field declarations — inherited from BaseTest

    private void goToManagerDashboard() {
        homePage.clickManagerLogin();
        logger.info("Navigated to Manager Dashboard");
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
        addCustomerPage.addCustomer(
                TestConstants.MGR_FIRST_NAME,
                TestConstants.MGR_LAST_NAME,
                TestConstants.MGR_POST_CODE);

        String alertText = AlertHelper.acceptAndGetText(driver);
        assertTrue(alertText.contains("Customer added successfully"),
                "Alert should confirm customer was added");
    }

    @ParameterizedTest(name = "M1b - Add customer: {0} {1}")
    @Order(2)
    @Story("Add Customer")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify multiple customers can be added with different valid data from JSON")
    @MethodSource("com.hasby.xyzbank.utils.TestDataProvider#customerData")
    void testAddMultipleCustomers(String firstName, String lastName, String postCode) {
        goToManagerDashboard();
        managerDashboard.clickAddCustomer();
        addCustomerPage.addCustomer(firstName, lastName, postCode);

        String alertText = AlertHelper.acceptAndGetText(driver);
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
        goToManagerDashboard();
        managerDashboard.clickAddCustomer();
        addCustomerPage.addCustomer(
                TestConstants.MGR_FIRST_NAME,
                TestConstants.MGR_LAST_NAME,
                TestConstants.MGR_POST_CODE);
        AlertHelper.acceptAndGetText(driver);

        managerDashboard.clickOpenAccount();
        openAccountPage.openAccount(
                TestConstants.MGR_FIRST_NAME + " " + TestConstants.MGR_LAST_NAME,
                TestConstants.CURRENCY);

        String alertText = AlertHelper.acceptAndGetText(driver);
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
        addCustomerPage.addCustomer(
                TestConstants.MGR_FIRST_NAME,
                TestConstants.MGR_LAST_NAME,
                TestConstants.MGR_POST_CODE);
        AlertHelper.acceptAndGetText(driver);

        managerDashboard.clickCustomers();
        assertTrue(customersPage.isCustomerPresent(TestConstants.MGR_FIRST_NAME),
                "Customer should be in the list");
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
        addCustomerPage.addCustomer(
                TestConstants.MGR_FIRST_NAME,
                TestConstants.MGR_LAST_NAME,
                TestConstants.MGR_POST_CODE);
        AlertHelper.acceptAndGetText(driver);

        managerDashboard.clickCustomers();
        customersPage.deleteCustomer(TestConstants.MGR_FIRST_NAME);

        assertFalse(customersPage.isCustomerPresent(TestConstants.MGR_FIRST_NAME),
                "Customer should be deleted from the list");
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
        addCustomerPage.addCustomer(
                TestConstants.MGR_FIRST_NAME,
                TestConstants.MGR_LAST_NAME,
                TestConstants.MGR_POST_CODE);
        AlertHelper.acceptAndGetText(driver);

        managerDashboard.clickCustomers();
        customersPage.deleteCustomer(TestConstants.MGR_FIRST_NAME);

        driver.get(BASE_URL);
        initPages();
        homePage.clickCustomerLogin();

        assertFalse(customerLoginPage.isCustomerInDropdown(
                        TestConstants.MGR_FIRST_NAME + " " + TestConstants.MGR_LAST_NAME),
                "Deleted customer should not appear in login dropdown");
    }

    // ==================== NEGATIVE / BUG TESTS (run and FAIL to prove bugs) ====================

    @Test
    @Order(7)
    @Story("Input Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("BUG-001 | Customer name field accepts numbers. " +
            "Expected: Only alphabetic characters allowed. " +
            "Actual: 'John123' is accepted without validation error.")
    @DisplayName("M2 - Name should reject numbers")
    void testNameRejectsNumbers() {
        goToManagerDashboard();
        managerDashboard.clickAddCustomer();
        addCustomerPage.addCustomer("John123", TestConstants.MGR_LAST_NAME, TestConstants.MGR_POST_CODE);

        String alertText = AlertHelper.acceptAndGetText(driver);
        assertFalse(alertText.contains("Customer added successfully"),
                "Name with numbers should be rejected");
    }

    @Test
    @Order(8)
    @Story("Input Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("BUG-002 | Customer name field accepts special characters. " +
            "Expected: Only alphabetic characters allowed. " +
            "Actual: 'John@#$' is accepted without validation error.")
    @DisplayName("M3 - Name should reject special characters")
    void testNameRejectsSpecialChars() {
        goToManagerDashboard();
        managerDashboard.clickAddCustomer();
        addCustomerPage.addCustomer("John@#$", TestConstants.MGR_LAST_NAME, TestConstants.MGR_POST_CODE);

        String alertText = AlertHelper.acceptAndGetText(driver);
        assertFalse(alertText.contains("Customer added successfully"),
                "Name with special characters should be rejected");
    }

    @Test
    @Order(9)
    @Story("Input Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("BUG-003 | Postal code field accepts non-numeric input. " +
            "Expected: Only numeric characters allowed. " +
            "Actual: 'ABCDEF' is accepted without validation error.")
    @DisplayName("M4 - Postal code should reject non-numeric")
    void testPostalCodeRejectsNonNumeric() {
        goToManagerDashboard();
        managerDashboard.clickAddCustomer();
        addCustomerPage.addCustomer(TestConstants.MGR_FIRST_NAME, TestConstants.MGR_LAST_NAME, "ABCDEF");

        String alertText = AlertHelper.acceptAndGetText(driver);
        assertFalse(alertText.contains("Customer added successfully"),
                "Non-numeric postal code should be rejected");
    }
}