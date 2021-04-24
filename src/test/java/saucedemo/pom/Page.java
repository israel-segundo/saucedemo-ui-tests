package saucedemo.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * Represents a PageObjectModel primitive object
 */
public abstract class Page {

    protected final WebDriver driver;

    public Page(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public abstract void waitUntilPageIsLoaded();
}
