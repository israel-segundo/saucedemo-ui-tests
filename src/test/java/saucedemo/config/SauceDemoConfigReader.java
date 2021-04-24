package saucedemo.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * This class creates a configuration reader object for file "production.properties". Such file contains test
 * configuration of our framework.
 */
public class SauceDemoConfigReader {

    public static SauceDemoConfigReader INSTANCE;
    private final Logger log = Logger.getLogger(SauceDemoConfigReader.class.getName());

    private static final String SAUCE_DEMO_CONFIG_FILE = "production.properties";
    private Properties properties;

    // Property Names
    public static final String SAUCE_DEMO_WEB_URL = "saucedemo.web.url";
    public static final String DEFAULT_WAIT_TIME_IN_SECONDS = "default.wait.time.in.seconds";

    public static final String SAUCE_DEMO_VALID_USER_NAME = "saucedemo.valid.user.name";
    public static final String SAUCE_DEMO_VALID_USER_PASSWORD = "saucedemo.valid.user.pwd";
    public static final String SAUCE_DEMO_INVALID_USER_NAME = "saucedemo.invalid.user.name";
    public static final String SAUCE_DEMO_INVALID_USER_PASSWORD = "saucedemo.invalid.user.pwd";

    public static final String GECKO_DRIVER_PATH = "firefox.geckodriver.path";
    public static final String CHROME_DRIVER_PATH = "chrome.chromedriver.path";
    public static final String EDGE_DRIVER_PATH = "edge.driver.path";

    public static SauceDemoConfigReader getInstance() {
        if (Objects.isNull(INSTANCE)) {
            try {
                INSTANCE = new SauceDemoConfigReader();
            } catch (FileNotFoundException | InvalidPropertiesFormatException exception) {
                throw new RuntimeException("Cannot initialize SauceDemoConfigReader object. Reason:" + exception.getMessage(), exception);
            }
        }
        return INSTANCE;
    }
    private SauceDemoConfigReader() throws FileNotFoundException, InvalidPropertiesFormatException {
        properties = new Properties();

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(SAUCE_DEMO_CONFIG_FILE);

        if (inputStream != null) {
            try {
                properties.load(inputStream);
                log.info(String.format("Configuration loaded from %s: %s", SAUCE_DEMO_CONFIG_FILE, properties.toString()));

            } catch (IOException ioException) {
                throw new InvalidPropertiesFormatException(
                        "content of property file " + SAUCE_DEMO_CONFIG_FILE + " is not on correct format");
            }
        } else {
            throw new FileNotFoundException("property file " + SAUCE_DEMO_CONFIG_FILE + " not found in the classpath");
        }
    }

    public int getIntValueFromConfig(final String configPropertyName, final int defaultWaitTimeInSeconds) {
        String value = getValueFromConfig(configPropertyName);

        if (Objects.nonNull(value)) {
            // Attempt to parse to int
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException numberFormatException) {
                // Property not parsable to int; falling back to default value.
            }
        }
        return defaultWaitTimeInSeconds;
    }

    public String getValueFromConfig(final String configPropertyName) {
        if (Objects.nonNull(configPropertyName) && Objects.nonNull(properties)) {
            return properties.getProperty(configPropertyName);
        }
        return null;
    }

    public String getValueFromConfig(final String configPropertyName, final String defaultValue) {
        String value = getValueFromConfig(configPropertyName);
        if (Objects.nonNull(value)) {
            return value;
        }
        return defaultValue;
    }
}
