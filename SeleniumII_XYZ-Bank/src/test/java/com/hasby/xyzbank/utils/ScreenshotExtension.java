package com.hasby.xyzbank.utils;

import com.hasby.xyzbank.base.BaseTest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScreenshotExtension implements TestWatcher {
    private static final Logger logger = LoggerFactory.getLogger(ScreenshotExtension.class);

    // Called automatically by JUnit when a test FAILS
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        BaseTest test = (BaseTest) context.getRequiredTestInstance();
        String testName = context.getDisplayName();

        if (test.getDriver() != null) {
            logger.info("Test FAILED — capturing screenshot: {}", testName);
            ScreenshotUtil.takeScreenshot(test.getDriver(), "FAILED_" + testName);
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
