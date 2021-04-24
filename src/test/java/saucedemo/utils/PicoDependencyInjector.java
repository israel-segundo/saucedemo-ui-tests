package saucedemo.utils;

import saucedemo.pom.*;
import io.cucumber.core.backend.ObjectFactory;
import io.cucumber.picocontainer.PicoFactory;

/**
 * This class defines a container for all objects that will be injected across the framework.
 */
public class PicoDependencyInjector implements ObjectFactory {

    private PicoFactory delegate = new PicoFactory();

    public PicoDependencyInjector() {
        addClass(SharedWebDriver.class);
        addClass(LoginPage.class);
        addClass(InventoryPage.class);
        addClass(ShoppingCartPage.class);
        addClass(CheckoutDataFormPage.class);
        addClass(CheckoutOverviewPage.class);
        addClass(CheckoutCompletePage.class);
        addClass(BurgerMenuComponent.class);
        addClass(ShoppingCartComponent.class);
    }

    @Override
    public void start() {
        delegate.start();
    }

    @Override
    public void stop() {
        delegate.stop();
    }

    @Override
    public boolean addClass(Class<?> aClass) {
        return delegate.addClass(aClass);
    }

    @Override
    public <T> T getInstance(Class<T> aClass) {
        return delegate.getInstance(aClass);
    }
}
