package saucedemo.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import saucedemo.config.SauceDemoConfigReader;

/**
 * This class contains utility functions related to selenium that can be used by any step or page object
 */
public class WebDriverUtils {

    static SauceDemoConfigReader config = SauceDemoConfigReader.getInstance();

    /**
     * Waits until a given WebElement object is clickable.
     * @param driver WebDriver object containing the current session
     * @param webElement WebElement object that will be verified against
     * @throws TimeoutException if the element does not become clickable after the configured timeout of the framework (check SauceDemoConfigReader)
     */
    public static void waitUntilElementIsClickable(WebDriver driver, WebElement webElement) throws TimeoutException {
        try {
            WebDriverWait wait = new WebDriverWait(driver, config.getIntValueFromConfig(SauceDemoConfigReader.DEFAULT_WAIT_TIME_IN_SECONDS, 10));
            wait.until(ExpectedConditions.elementToBeClickable(webElement));
        } catch (Exception ex) {
            throw new TimeoutException("Timeout exceeded while waiting for element to be clickable.");
        }
    }

    /**
     * Waits until a given WebElement object is visible.
     * @param driver WebDriver object containing the current session
     * @param webElement WebElement object that will be verified against
     * @throws TimeoutException if the element does not become visible after the configured timeout of the framework (check SauceDemoConfigReader)
     */
    public static void waitUntilElementIsVisible(WebDriver driver, WebElement webElement) throws TimeoutException {
        try {
            WebDriverWait wait = new WebDriverWait(driver, config.getIntValueFromConfig(SauceDemoConfigReader.DEFAULT_WAIT_TIME_IN_SECONDS, 10));
            wait.until(ExpectedConditions.visibilityOf(webElement));
        } catch (Exception ex) {
            throw new TimeoutException("Timeout exceeded while waiting for element to appear.");
        }
    }

    /**
     * Checks if the current page contains a given text.
     * @param driver WebDriver object containing the current session
     * @param message Text that will be searched
     * @return True if text is displayed on the current page. False otherwise.
     */
    public static boolean pageContainsTextMessage(WebDriver driver, String message) {
        try {
            String containsTextXpath = String.format("//*[contains(text(), '%s')]", message);
            driver.findElement(By.xpath(containsTextXpath));
            // Element found
            return true;
        } catch (Exception exception) {
            // Element not found
        }
        return false;
    }
}
