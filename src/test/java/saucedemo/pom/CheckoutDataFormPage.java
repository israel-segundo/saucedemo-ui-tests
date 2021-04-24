package saucedemo.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import saucedemo.utils.WebDriverUtils;

import java.util.logging.Logger;

/**
 * PageObject that provides interaction mechanisms for the checkout:dataform Page.
 */
public class CheckoutDataFormPage extends Page {

    private final Logger log = Logger.getLogger(CheckoutDataFormPage.class.getName());

    @FindBy(id = "first-name")
    WebElement firstNameTextBox;

    @FindBy(id = "last-name")
    WebElement lastNameTextBox;

    @FindBy(id = "postal-code")
    WebElement postalCodeTextBox;

    @FindBy(id = "continue")
    WebElement continueBtn;

    public CheckoutDataFormPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void waitUntilPageIsLoaded() {
        log.info("Waiting for CheckoutDataFormPage to load...");
        try {
            WebDriverUtils.waitUntilElementIsVisible(driver, continueBtn);
        } catch (Exception ex) {
            throw new RuntimeException("Timeout exceeded while waiting for checkout data form page to load.");
        }
        log.info("CheckoutDataFormPage finished loading.");
    }

    public void fillCheckoutInformation(final String firstName, final String lastName, final String postalCode) {
        firstNameTextBox.clear();
        firstNameTextBox.sendKeys(firstName);

        lastNameTextBox.clear();
        lastNameTextBox.sendKeys(lastName);

        postalCodeTextBox.clear();
        postalCodeTextBox.sendKeys(postalCode);
    }

    public void clickContinueButton() {
        WebDriverUtils.waitUntilElementIsClickable(driver, continueBtn);
        continueBtn.click();
    }
}
