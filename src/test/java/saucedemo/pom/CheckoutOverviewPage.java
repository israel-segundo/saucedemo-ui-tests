package saucedemo.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import saucedemo.utils.WebDriverUtils;

import java.util.logging.Logger;

/**
 * PageObject that provides interaction mechanisms for the checkout:overview Page.
 */
public class CheckoutOverviewPage extends Page {


    private final Logger log = Logger.getLogger(CheckoutOverviewPage.class.getName());

    @FindBy(id = "checkout_summary_container")
    WebElement summaryContainer;

    @FindBy(id = "finish")
    WebElement finishBtn;

    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void waitUntilPageIsLoaded() {
        log.info("Waiting for CheckoutOverviewPage to load...");
        try {
            WebDriverUtils.waitUntilElementIsVisible(driver, finishBtn);
        } catch (Exception ex) {
            throw new RuntimeException("Timeout exceeded while waiting for checkout:overview page to load.");
        }
        log.info("CheckoutOverviewPage finished loading.");
    }

    public void clickFinishButton() {
        WebDriverUtils.waitUntilElementIsClickable(driver, finishBtn);
        finishBtn.click();
    }

    public boolean isOrderSummaryPresent() {
        try {
            WebDriverUtils.waitUntilElementIsVisible(driver, summaryContainer);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
}
