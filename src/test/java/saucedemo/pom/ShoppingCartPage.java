package saucedemo.pom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import saucedemo.pom.beans.Product;
import saucedemo.utils.WebDriverUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * PageObject that provides interaction mechanisms for the Shopping Cart Page.
 */
public class ShoppingCartPage extends Page {

    private final Logger log = Logger.getLogger(ShoppingCartPage.class.getName());

    public ShoppingCartPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(id = "cart_contents_container")
    WebElement cartContainer;

    @FindBy(className = "cart_item")
    List<WebElement> cartItemsWebElements;

    @FindBy(id = "checkout")
    WebElement checkoutBtn;

    // Locators for Products
    private static final String PRODUCT_NAME_CLASSNAME_LOCATOR = "inventory_item_name";
    private static final String PRODUCT_DESCRIPTION_CLASSNAME_LOCATOR = "inventory_item_desc";
    private static final String PRODUCT_PRICE_CLASSNAME_LOCATOR = "inventory_item_price";

    public void waitUntilPageIsLoaded() {
        log.info("Waiting for ShoppingCartPage to load...");
        try {
            WebDriverUtils.waitUntilElementIsVisible(driver, cartContainer);
        } catch (Exception ex) {
            throw new RuntimeException("Timeout exceeded while waiting for shopping cart page to load.");
        }
        log.info("ShoppingCartPage finished loading.");
    }

    public List<Product> getProductsInCart() {
        LinkedList<Product> products = new LinkedList<>();
        for (WebElement productWebElement: cartItemsWebElements) {
            try {
                String name = productWebElement.findElement(By.className(PRODUCT_NAME_CLASSNAME_LOCATOR)).getText();
                String description = productWebElement.findElement(By.className(PRODUCT_DESCRIPTION_CLASSNAME_LOCATOR)).getText();
                String priceStr = productWebElement.findElement(
                        By.className(PRODUCT_PRICE_CLASSNAME_LOCATOR)).getText().replace("$", "");
                Double price = Double.parseDouble(priceStr);

                products.add(new Product(name, description, price));
            } catch (Exception ex) {
                log.warning("Could not parse WebElement and build a Product." + ex.getMessage());
            }
        }
        return products;
    }

    public boolean isProductOnCart(final String expectedProductName) {
        List<Product> productsInCart = getProductsInCart();
        log.info(String.format("Products in cart: %s", productsInCart));
        return productsInCart.stream()
                             .map(Product::getName)
                             .anyMatch(productName -> productName.equalsIgnoreCase(expectedProductName));
    }

    public void clickOnCheckoutButton() {
        WebDriverUtils.waitUntilElementIsClickable(driver, checkoutBtn);
        checkoutBtn.click();
    }
}
