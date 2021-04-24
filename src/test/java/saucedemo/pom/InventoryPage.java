package saucedemo.pom;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import saucedemo.pom.beans.Product;
import saucedemo.utils.WebDriverUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

/**
 * PageObject that provides interaction mechanisms for the Inventory Page.
 */
public class InventoryPage extends Page {
    private final Logger log = Logger.getLogger(InventoryPage.class.getName());

    @FindBy(className = "inventory_item")
    List<WebElement> inventoryWebElements;

    @FindBy(id = "inventory_container")
    WebElement inventoryContainer;

    @FindBy(className = "product_sort_container")
    WebElement sortDropdownWebElement;

    // Component to interact with the shopping cart icon
    ShoppingCartComponent shoppingCartComponent;

    // Locators for Products
    private static final String PRODUCT_NAME_CLASSNAME_LOCATOR = "inventory_item_name";
    private static final String PRODUCT_DESCRIPTION_CLASSNAME_LOCATOR = "inventory_item_desc";
    private static final String PRODUCT_PRICE_CLASSNAME_LOCATOR = "inventory_item_price";
    private static final String PRODUCT_ADD_REMOVE_TO_CART_LOCATOR = "btn_primary";

    public InventoryPage(WebDriver driver) {
        super(driver);
        shoppingCartComponent = new ShoppingCartComponent(driver);
    }

    public void waitUntilPageIsLoaded() {
        log.info("Waiting for InventoryPage to load...");
        try {
            WebDriverUtils.waitUntilElementIsVisible(driver, inventoryContainer);
        } catch (Exception ex) {
            throw new RuntimeException("Timeout exceeded while waiting for inventory page to load.");
        }
        log.info("InventoryPage finished loading.");
    }

    public void sortProductsByCriteria(String criteria){
        WebDriverUtils.waitUntilElementIsClickable(driver, sortDropdownWebElement);
        log.info("Clicking on the sort dropdown...");
        sortDropdownWebElement.click();

        Select sortDropdown = new Select(sortDropdownWebElement);
        try {
            log.info(String.format("Attempting to select criteria: %s on the dropdown", criteria));
            sortDropdown.selectByVisibleText(criteria);
        } catch (NoSuchElementException noSuchElementException) {
            throw new WebDriverException(String.format("Sort criteria %s was not available.", criteria), noSuchElementException);
        }
    }

    public List<Product> getProducts() {
        LinkedList<Product> products = new LinkedList<>();
        for (WebElement productWebElement: inventoryWebElements) {
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

    public void goToShoppingCart() {
        shoppingCartComponent.waitUntilVisible();
        shoppingCartComponent.goToShoppingCart();
    }


    public boolean addProductToShoppingCart(final String productName) {
        log.info(String.format("Searching product %s on the inventory list...", productName));
        for (WebElement productWebElement: inventoryWebElements) {
            try {
                String name = productWebElement.findElement(By.className(PRODUCT_NAME_CLASSNAME_LOCATOR)).getText();
                if (productName.equalsIgnoreCase(name)) {
                    log.info(String.format("Found product %s on inventory list. Attempting to add it to cart.", productName));
                    productWebElement.findElement(By.className(PRODUCT_ADD_REMOVE_TO_CART_LOCATOR)).click();
                    log.info(String.format("Product %s was added to the cart.", productName));
                    return true;
                }
            } catch (WebDriverException webDriverException) {
                log.severe(String.format("Unable to add product %s to cart. Reason: %s", productName, webDriverException.getMessage()));
            }
        }
        return false;
    }
}
