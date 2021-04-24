package saucedemo.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import saucedemo.utils.WebDriverUtils;

import java.util.logging.Logger;

/**
 * Page object that contains mechanisms to interact with the checkout/complete page of sauce demo.
 */
public class CheckoutCompletePage extends Page {

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    private final Logger log = Logger.getLogger(CheckoutCompletePage.class.getName());

    @FindBy(id = "back-to-products")
    WebElement backToHomeBtn;

    @Override
    public void waitUntilPageIsLoaded() {
        log.info("Waiting for CheckoutCompletePage to load...");
        try {
            WebDriverUtils.waitUntilElementIsVisible(driver, backToHomeBtn);
        } catch (Exception ex) {
            throw new RuntimeException("Timeout exceeded while waiting for CheckoutCompletePage page to load.");
        }
        log.info("CheckoutCompletePage finished loading.");
    }
}
