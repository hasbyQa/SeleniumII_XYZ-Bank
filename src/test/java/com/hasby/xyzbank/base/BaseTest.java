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
public abstract class BaseTest {

    protected WebDriver driver;
    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    protected static final String BASE_URL =
            "https://www.way2automation.com/angularjs-protractor/banking/#/login";

    @BeforeEach
    void setUp() {
        logger.info("*************** TEST SETUP STARTED ***************");
        driver = DriverFactory.createDriver();
        driver.get(BASE_URL);
        logger.info("Navigated to: {}", BASE_URL);
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