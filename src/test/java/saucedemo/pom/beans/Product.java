package saucedemo.pom.beans;

import java.util.Objects;

/**
 * Represents a Product from inventory or shopping cart
 */
public class Product {

    private String name;
    private String description;
    private Double price;

    public Product(String name, String description, Double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public Double getPrice() {
        return price;
    }

    public String toString() {
        return String.format("{name=%s, description=%s, price=%f}", name, description, price);
    }

    public boolean equals(Product product) {
        if (Objects.isNull(product)) {
            return false;
        }
        return getName().equalsIgnoreCase(product.getName())
                && getPrice().equals(product.getPrice())
                && getDescription().equals(product.getDescription());
    }
}
