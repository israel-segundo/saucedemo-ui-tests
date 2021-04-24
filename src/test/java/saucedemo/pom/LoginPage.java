package saucedemo.pom;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import saucedemo.config.SauceDemoConfigReader;
import saucedemo.utils.WebDriverUtils;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * Page object that contains mechanisms to interact with the login page of sauce demo.
 */
public class LoginPage extends Page {

    private final Logger log = Logger.getLogger(LoginPage.class.getName());
    static SauceDemoConfigReader config = SauceDemoConfigReader.getInstance();

    @FindBy(id = "login-button")
    WebElement loginButton;

    @FindBy(id = "user-name")
    WebElement userNameTextInput;

    @FindBy(id = "password")
    WebElement passwordInput;

    @FindBy(className = "error-message-container")
    WebElement errorMessageContainer;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void goTo() {
        // Load url from config file. If not found use default value.
        String url = config.getValueFromConfig(SauceDemoConfigReader.SAUCE_DEMO_WEB_URL, "https://www.saucedemo.com/") ;
        log.info("Navigating to " + url);

        driver.get(url);
    }

    public void login(final String userName, final String password) {
        log.info(String.format("Entering credentials:  [User=%s, Password=*****]", userName));
        userNameTextInput.clear();
        userNameTextInput.sendKeys(userName);
        passwordInput.clear();
        passwordInput.sendKeys(password);

        log.info("Submitting credentials...");
        loginButton.click();
    }

    public String getErrorMessage() {
        WebDriverUtils.waitUntilElementIsVisible(driver, errorMessageContainer);
        String text = errorMessageContainer.getText();
        if (Objects.nonNull(text)) {
            return text.trim();
        }
        return "";
    }

    public void waitUntilPageIsLoaded() {
        try {
            WebDriverUtils.waitUntilElementIsVisible(driver, loginButton);
            log.info("LoginPage finished loading");
        } catch (Exception ex) {
            throw new TimeoutException("Timeout exceeded while waiting for login page to load.");
        }
    }
}
