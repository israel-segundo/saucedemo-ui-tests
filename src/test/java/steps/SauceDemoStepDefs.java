package steps;

import com.google.common.collect.Ordering;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import org.openqa.selenium.TimeoutException;
import saucedemo.config.SauceDemoConfigReader;
import saucedemo.pom.*;
import saucedemo.pom.beans.Product;
import saucedemo.utils.SharedWebDriver;
import io.cucumber.java.en.Given;
import org.picocontainer.annotations.Inject;
import saucedemo.utils.WebDriverUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This file contains all the step definitions used by the framework.
 */
public class SauceDemoStepDefs {

    private final Logger log = Logger.getLogger(SauceDemoStepDefs.class.getName());
    SauceDemoConfigReader config = SauceDemoConfigReader.getInstance();

    @Inject private SharedWebDriver driver;
    @Inject private LoginPage loginPage;
    @Inject private InventoryPage inventoryPage;
    @Inject private ShoppingCartPage shoppingCartPage;
    @Inject private CheckoutDataFormPage checkoutDataFormPage;
    @Inject private CheckoutOverviewPage checkoutOverviewPage;
    @Inject private CheckoutCompletePage checkoutCompletePage;
    @Inject private BurgerMenuComponent burgerMenuComponent;
    @Inject private ShoppingCartComponent shoppingCartComponent;

    private List<Product> expectedProductsInCart;
    @Given("I am on the SauceDemo login page")
    public void i_am_on_the_saucedemo_login_page() {
        loginPage.goTo();
        loginPage.waitUntilPageIsLoaded();
    }

    @Given("I authenticate using valid credentials")
    public void i_authenticate_using_valid_credentials() {
        String username = config.getValueFromConfig(SauceDemoConfigReader.SAUCE_DEMO_VALID_USER_NAME);
        String password = config.getValueFromConfig(SauceDemoConfigReader.SAUCE_DEMO_VALID_USER_PASSWORD);

        loginPage.login(username, password);
    }

    @Given("I can see the inventory page")
    public void i_can_see_the_inventory_page() {
        inventoryPage.waitUntilPageIsLoaded();
    }

    @Given("I authenticate using invalid credentials")
    public void i_authenticate_using_invalid_credentials() {
        String username = config.getValueFromConfig(SauceDemoConfigReader.SAUCE_DEMO_INVALID_USER_NAME);
        String password = config.getValueFromConfig(SauceDemoConfigReader.SAUCE_DEMO_INVALID_USER_PASSWORD);

        loginPage.login(username, password);
    }

    @Then("I should see an error saying {string}")
    public void i_should_see_an_error_saying(final String expectedErrorMessage) {
        String errorMessage = "";

        try {
            errorMessage = loginPage.getErrorMessage();
        } catch (TimeoutException timeoutException) {
            Assert.fail("Error message did not appear on the LoginPage");
        }

        log.info("Error message obtained: " + errorMessage);
        Assert.assertTrue("An error message indicating the invalid login should be displayed",
                expectedErrorMessage.equalsIgnoreCase(errorMessage));
    }

    @Given("I logout from the site")
    public void i_logout_from_the_site() {
        burgerMenuComponent.logout();
    }

    @Then("I am redirected to the SauceDemo login page")
    public void i_am_redirected_to_the_saucedemo_login_page() {
        loginPage.waitUntilPageIsLoaded();
    }

    @Given("I sort the products using criteria {string} using the sort dropdown")
    public void i_sort_the_products_using_dropdown(final String sortCriteria) {
        inventoryPage.sortProductsByCriteria(sortCriteria);
    }

    @Then("The products are sorted by price \\(low to high)")
    public void the_products_are_sorted_by_price_low_to_high() {
        List<Product> products = inventoryPage.getProducts();
        log.info("List of products obtained: " + products);

        List<Double> prices = products.stream().map(Product::getPrice).collect(Collectors.toList());
        log.info("List of prices obtained: " + prices);

        Assert.assertTrue("Prices obtained should be ordered from low to high", Ordering.natural().isOrdered(prices));
    }

    @Given("I add product {string} to the shopping cart")
    public void i_add_product_to_the_shopping_cart(final String productName) {
        Assert.assertTrue(
            String.format("Product %s should be added to the cart", productName),
            inventoryPage.addProductToShoppingCart(productName)
        );
    }

    @Given("I add {int} products to the shopping cart")
    public void i_add_x_products_to_the_shopping_cart(final int numberOfProducts) {
        List<Product> products = inventoryPage.getProducts();
        log.info("List of products obtained: " + products);

        // Validating that number of products specified is valid.
        if (numberOfProducts <= 0) {
            Assert.fail("Number of products specified cannot be less or equal than zero.");
        }

        if (products.size() == 0) {
            Assert.fail("There are no products on the inventory list.");
        }

        int productsToAdd = numberOfProducts;
        if (numberOfProducts > products.size()) {
            log.info(String.format("The amount of products specified (%d) exceeds the number of products " +
                    "present on the inventory list (%d). This test will add only %d products to the cart.",
                    numberOfProducts, products.size(), products.size()));
            productsToAdd = products.size();
        }

        // Storing the products that will be added to cart on a list, so we can validate on another step:
        expectedProductsInCart = new LinkedList<>();

        // Selecting the first ${productsToAdd} products.
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            if (inventoryPage.addProductToShoppingCart(product.getName())){
                expectedProductsInCart.add(product);
            } else {
                Assert.fail(String.format("Unable to add product %s to cart.", product.getName()));
            }
        }
    }

    @Then("the products previously added should be on the shopping cart")
    public void the_same_x_products_should_be_on_the_shopping_cart() {
        try {
            log.info("Verifying that the following products are on cart: " + expectedProductsInCart);

            if (Objects.isNull(expectedProductsInCart) || expectedProductsInCart.isEmpty()) {
                Assert.fail("There is no recorded/expected products registered. Cannot execute this test");
            }

            List<Product> productsOnCart = shoppingCartPage.getProductsInCart();
            log.info(String.format("Products on cart: %s", productsOnCart));

            for (Product expectedProduct : expectedProductsInCart) {
                boolean isProductOnCart = productsOnCart.stream().anyMatch(product -> product.equals(expectedProduct));

                if (isProductOnCart) {
                    log.info(String.format("Product %s appears to be on cart", expectedProduct));
                } else {
                    Assert.fail(String.format("Product %s is not on the cart", expectedProduct));
                }
            }
            log.info("All expected products are on cart.");

        } finally {
            // Reset expected product list.
            expectedProductsInCart = new LinkedList<>();
        }
    }

    @And("I navigate to the shopping cart page")
    public void i_navigate_to_the_shopping_cart_page() {
        inventoryPage.goToShoppingCart();
        shoppingCartPage.waitUntilPageIsLoaded();
    }

    @Then("the {string} product should be on the shopping cart")
    public void the_product_should_be_on_the_shopping_cart(final String productName) {
        Assert.assertTrue(
            String.format("Product %s should be on the shopping cart", productName),
            shoppingCartPage.isProductOnCart(productName)
        );
    }

    @Given("I click on the checkout button")
    public void i_click_on_the_checkout_button() {
        shoppingCartPage.clickOnCheckoutButton();
        checkoutDataFormPage.waitUntilPageIsLoaded();
    }

    @Then("I enter first name {string}, last name {string} and postal code {string} on the checkout data form")
    public void i_enter_first_name_last_name_and_postal_code_on_the_checkout_data_form(final String firstName,
                                                                                       final String lastName,
                                                                                       final String postalCode) {
        checkoutDataFormPage.fillCheckoutInformation(firstName, lastName, postalCode);
    }

    @Given("I click continue")
    public void i_click_continue() {
        checkoutDataFormPage.clickContinueButton();
        checkoutOverviewPage.waitUntilPageIsLoaded();
    }

    @Then("I should see a summary of my order")
    public void i_should_see_a_summary_of_my_order() {
        Assert.assertTrue(
    "The order summary should be displayed on the page",
            checkoutOverviewPage.isOrderSummaryPresent()
        );
    }

    @Given("I click Finish")
    public void i_click_finish() {
        checkoutOverviewPage.clickFinishButton();
        checkoutCompletePage.waitUntilPageIsLoaded();
    }

    @Then("I should see the {string} message on the screen")
    public void i_should_see_the_message(final String message) {
        Assert.assertTrue(
            String.format("Message %s should be displayed on the page", message),
            WebDriverUtils.pageContainsTextMessage(driver, message)
        );
    }

    @And("I reset the app state")
    public void i_reset_the_app_state() {
        burgerMenuComponent.resetAppState();
    }
}
