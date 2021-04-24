package saucedemo.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import saucedemo.config.SauceDemoConfigReader;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * This class setup the webdriver object that will used during testing.
 * The webdriver object will be injected on all the step/page object classes.
 */
public class SharedWebDriver extends EventFiringWebDriver {
    private static final Logger log = Logger.getLogger(SharedWebDriver.class.getName());

    private static WebDriver driver = null;

    private static final String TARGET_BROWSER_PROPERTY_NAME = "targetBrowser";

    // Supported Browsers
    private static final String FIREFOX = "firefox";
    private static final String CHROME = "chrome";
    private static final String EDGE = "edge";
    private static final String NOT_SPECIFIED = "NOT_SPECIFIED";

    private static final String FI = "targetBrowser";

    public SharedWebDriver() {
        super(driver);
    }

    private static final Thread CLOSE_DRIVER_THREAD = new Thread() {
        @Override
        public void run() {
            driver.quit();
        }
    };

    static {
        SauceDemoConfigReader config = SauceDemoConfigReader.getInstance();

        // Target browser name can be specified as a JVM System Property called "targetBrowser"
        // If not specified, defaulting to firefox.
        String targetBrowser = System.getProperty(TARGET_BROWSER_PROPERTY_NAME, NOT_SPECIFIED);
        log.info("Target Browser Specified: "  + targetBrowser);

        if (targetBrowser.equalsIgnoreCase(NOT_SPECIFIED)) {
            log.info("Target Browser defaulting to "  + FIREFOX);
            targetBrowser = FIREFOX;
        }

        switch(targetBrowser) {
            case CHROME:
                System.setProperty("webdriver.chrome.driver", config.getValueFromConfig(SauceDemoConfigReader.CHROME_DRIVER_PATH));
                driver = new ChromeDriver();
                break;
            case EDGE:
                System.setProperty("webdriver.edge.driver", config.getValueFromConfig(SauceDemoConfigReader.EDGE_DRIVER_PATH));
                driver = new EdgeDriver();
                break;
            case FIREFOX:
            default:
                System.setProperty("webdriver.gecko.driver", config.getValueFromConfig(SauceDemoConfigReader.GECKO_DRIVER_PATH));
                driver = new FirefoxDriver();
                break;
        }

        if (Objects.isNull(driver)) {
            throw new RuntimeException("Unable to initialize webdriver session");
        }

        driver.manage().timeouts().implicitlyWait(
                config.getIntValueFromConfig(
                                SauceDemoConfigReader.DEFAULT_WAIT_TIME_IN_SECONDS, 5),
                                TimeUnit.SECONDS
        );
        Runtime.getRuntime().addShutdownHook(CLOSE_DRIVER_THREAD);
    }
}
