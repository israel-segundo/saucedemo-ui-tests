package saucedemo.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import saucedemo.utils.WebDriverUtils;

import java.util.logging.Logger;

/**
 * This class allows to interact with the shopping cart icon that can be seen on multiple pages.
 */
public class ShoppingCartComponent {
    private final Logger log = Logger.getLogger(ShoppingCartComponent.class.getName());
    private final WebDriver driver;

    @FindBy(className = "shopping_cart_link")
    WebElement shoppingCartBtn;

    public ShoppingCartComponent(WebDriver driver) {
        this.driver =  driver;
        PageFactory.initElements(driver, this);
    }

    public void waitUntilVisible() {
        WebDriverUtils.waitUntilElementIsClickable(driver, shoppingCartBtn);
    }

    public void goToShoppingCart() {
        log.info("Clicking on the shopping cart button...");
        shoppingCartBtn.click();
    }
}
