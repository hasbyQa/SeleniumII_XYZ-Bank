package com.hasby.xyzbank.utils;

import com.hasby.xyzbank.base.BaseTest;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.util.Optional;

public class ScreenshotExtension implements TestWatcher {

    private static final Logger logger = LoggerFactory.getLogger(ScreenshotExtension.class);

    // Called automatically by JUnit when a test FAILS
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        BaseTest test = (BaseTest) context.getRequiredTestInstance();
        String testName = context.getDisplayName();

        if (test.getDriver() != null) {
            logger.info("Test FAILED — capturing screenshot: {}", testName);

            // Save to disk
            ScreenshotUtil.takeScreenshot(test.getDriver(), "FAILED_" + testName);

            // Attach to Allure report so it appears in the test's detail page
            byte[] screenshot = ((TakesScreenshot) test.getDriver()).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Screenshot on failure", "image/png",
                    new ByteArrayInputStream(screenshot), ".png");

            test.getDriver().quit();
            logger.info("Browser closed after failure");
        }
    }

    // Called automatically by JUnit when a test PASSES
    @Override
    public void testSuccessful(ExtensionContext context) {
        BaseTest test = (BaseTest) context.getRequiredTestInstance();

        if (test.getDriver() != null) {
            test.getDriver().quit();
            logger.info("Browser closed after success");
        }
    }

    // Called automatically by JUnit when a test is SKIPPED (@Disabled)
    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        String testName = context.getDisplayName();
        String skipReason = reason.orElse("No reason provided");
        logger.info("Test SKIPPED — {}: {}", testName, skipReason);
    }

    // Called automatically by JUnit when a test is ABORTED
    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        BaseTest test = (BaseTest) context.getRequiredTestInstance();

        if (test.getDriver() != null) {
            test.getDriver().quit();
            logger.info("Browser closed after abort");
        }
    }
}