package com.hasby.xyzbank.base;

import com.hasby.xyzbank.utils.DriverFactory;
import com.hasby.xyzbank.utils.ScreenshotExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ExtendWith(ScreenshotExtension.class)
public class BaseTest {

    //    Protected so subclasses can access the driver directly
    protected WebDriver driver;
    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    protected static final String BASE_URL =
            "https://www.globalsqa.com/angularJs-protractor/BankingProject/#/login";

    //    Creates a fresh browser instance and navigates to the base URL
    @BeforeEach
    void setUp() {
        logger.info("*************** TEST SETUP STARTED ***************");
        driver = DriverFactory.createDriver();
        driver.get(BASE_URL);
        logger.info("Navigated to: {}", BASE_URL);
        logger.info("*************** TEST SETUP COMPLETED ***************");
    }

    //    Closes the browser and releases resources
    @AfterEach
    void tearDown() {
        logger.info("*************** TEST TEARDOWN ***************");
    }

    // Used by ScreenshotExtension to access the driver
    public WebDriver getDriver() {
        return driver;
    }
}
