package com.hasby.xyzbank.base;

import com.hasby.xyzbank.pages.common.HomePage;
import com.hasby.xyzbank.pages.customer.*;
import com.hasby.xyzbank.pages.manager.AddCustomerPage;
import com.hasby.xyzbank.pages.manager.CustomersPage;
import com.hasby.xyzbank.pages.manager.ManagerDashboardPage;
import com.hasby.xyzbank.pages.manager.OpenAccountPage;
import com.hasby.xyzbank.utils.ScreenshotExtension;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(ScreenshotExtension.class)
public abstract class BaseTest {

    protected WebDriver driver;
    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    protected static final String BASE_URL =
            "https://www.way2automation.com/angularjs-protractor/banking/#/login";

    // Page objects — initialized once in setUp(), inherited by all tests (DRY)
    protected HomePage homePage;
    protected ManagerDashboardPage managerDashboard;
    protected AddCustomerPage addCustomerPage;
    protected OpenAccountPage openAccountPage;
    protected CustomersPage customersPage;
    protected CustomerLoginPage customerLoginPage;
    protected AccountPage accountPage;
    protected DepositPage depositPage;
    protected WithdrawPage withdrawPage;
    protected TransactionsPage transactionsPage;

    private static WebDriver createDriver() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-search-engine-choice-screen");
        options.addArguments("--remote-allow-origins=*");

        logger.info("Running in HEADLESS mode");
        WebDriver driver = new ChromeDriver(options);
        logger.info("ChromeDriver created successfully");
        return driver;
    }

    // Initializes all page objects — PageFactory proxies resolve lazily
    protected void initPages() {
        homePage = new HomePage(driver);
        managerDashboard = new ManagerDashboardPage(driver);
        addCustomerPage = new AddCustomerPage(driver);
        openAccountPage = new OpenAccountPage(driver);
        customersPage = new CustomersPage(driver);
        customerLoginPage = new CustomerLoginPage(driver);
        accountPage = new AccountPage(driver);
        depositPage = new DepositPage(driver);
        withdrawPage = new WithdrawPage(driver);
        transactionsPage = new TransactionsPage(driver);
    }

    @BeforeEach
    void setUp() {
        logger.info("*************** TEST SETUP STARTED ***************");
        driver = createDriver();
        driver.get(BASE_URL);
        logger.info("Navigated to: {}", BASE_URL);
        initPages();
        logger.info("All page objects initialized");
        logger.info("*************** TEST SETUP COMPLETED ***************");
    }

    @AfterEach
    void tearDown() {
        logger.info("*************** TEST TEARDOWN ***************");
    }

    public WebDriver getDriver() {
        return driver;
    }
}