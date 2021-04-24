package saucedemo.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import saucedemo.utils.WebDriverUtils;

import java.util.logging.Logger;

/**
 * This class allows to interact with the burger menu icon that can be seen on multiple pages.
 */
public class BurgerMenuComponent {

    private final Logger log = Logger.getLogger(BurgerMenuComponent.class.getName());
    private final WebDriver driver;

    @FindBy(id = "react-burger-menu-btn")
    WebElement burgerMenuBtn;

    @FindBy(id = "logout_sidebar_link")
    WebElement logoutLink;

    @FindBy(id = "reset_sidebar_link")
    WebElement resetAppStateLink;

    public BurgerMenuComponent(WebDriver driver) {
        this.driver =  driver;
        PageFactory.initElements(driver, this);
    }

    public void waitUntilVisible() {
        WebDriverUtils.waitUntilElementIsClickable(driver, burgerMenuBtn);
    }

    public void logout() {
        waitUntilVisible();
        log.info("Clicking on the burger menu...");
        burgerMenuBtn.click();

        log.info("Waiting for the logout button to appear...");
        WebDriverUtils.waitUntilElementIsClickable(driver, logoutLink);

        log.info("Clicking on the logout link...");
        logoutLink.click();
    }

    public void resetAppState() {
        waitUntilVisible();
        log.info("Clicking on the burger menu...");
        burgerMenuBtn.click();

        log.info("Waiting for the reset app state button to appear...");
        WebDriverUtils.waitUntilElementIsClickable(driver, resetAppStateLink);

        log.info("Clicking on the reset app state link...");
        resetAppStateLink.click();
    }
}
