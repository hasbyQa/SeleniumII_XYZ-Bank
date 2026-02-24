package com.hasby.xyzbank.utils;

import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Handles JS alert popups — XYZ Bank uses alerts for all confirmations
public class AlertHelper {
    private static final Logger logger = LoggerFactory.getLogger(AlertHelper.class);

    private AlertHelper() {}

    // Accepts the alert and returns its text, or empty string if no alert
    public static String acceptAndGetText(WebDriver driver) {
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
}