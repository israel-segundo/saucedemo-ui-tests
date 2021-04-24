Feature: SauceDemo UI Automation Code Challenge
  Background:
    Given I am on the SauceDemo login page

  Scenario: Login with a valid user
    Given I authenticate using valid credentials
    Then I can see the inventory page

  Scenario: Login with an invalid user
    Given I authenticate using invalid credentials
    Then I should see an error saying "Epic sadface: Username and password do not match any user in this service"

  Scenario: Logout from the home page
    Given I authenticate using valid credentials
    Then I can see the inventory page
    Given I logout from the site
    Then I am redirected to the SauceDemo login page

  Scenario: Sort products by Price (low to high)
    Given I authenticate using valid credentials
    Then I can see the inventory page
    Given I sort the products using criteria "Price (low to high)" using the sort dropdown
    Then The products are sorted by price (low to high)

  Scenario: Add multiple items to the shopping cart
    Given I authenticate using valid credentials
    Then I can see the inventory page
    Given I add 3 products to the shopping cart
    And I navigate to the shopping cart page
    Then the products previously added should be on the shopping cart
    And I reset the app state

  Scenario: Add the specific product ‘Sauce Labs Onesie’ to the shopping cart
    Given I authenticate using valid credentials
    Then I can see the inventory page
    Given I add product "Sauce Labs Onesie" to the shopping cart
    And I navigate to the shopping cart page
    Then the "Sauce Labs Onesie" product should be on the shopping cart
    And I reset the app state

  Scenario: Complete a purchase
    Given I authenticate using valid credentials
    Then I can see the inventory page
    Given I add product "Sauce Labs Onesie" to the shopping cart
    And I navigate to the shopping cart page
    Then the "Sauce Labs Onesie" product should be on the shopping cart
    Given I click on the checkout button
    Then I enter first name "John", last name "Doe" and postal code "9001" on the checkout data form
    Given I click continue
    Then I should see a summary of my order
    Given I click Finish
    Then I should see the "Your order has been dispatched" message on the screen




