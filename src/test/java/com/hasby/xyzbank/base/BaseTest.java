package com.hasby.xyzbank.base;

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

    // Driver creation — configured for both local and CI/Docker environments
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

    @BeforeEach
    void setUp() {
        logger.info("*************** TEST SETUP STARTED ***************");
        driver = createDriver();
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