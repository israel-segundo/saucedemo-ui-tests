### Automation Challenge / Wizeline QA Challenge 2021
#### Author: israel.segundo@gmail.com


### Description
This repository contains a Java project that define the UI tests and related artifacts that can be used to test the UI of https://www.saucedemo.com/
. The test framework is designed to rely on Cucumber scenarios and JUnit. It currently supports testing using **Firefox**, **Chrome** and **Edge** browsers.

The test scenarios are defined inside file `src/test/resources/SauceDemo.feature` and they include:
- Login with a valid user
- Login with an invalid user
- Logout from the home page
- Sort products by Price (low to high)
- Add multiple items to the shopping cart
- Add the specific product ‘Sauce Labs Onesie’ to the shopping cart
- Complete a purchase

Notes: 
- Each cucumber step is mapped to specific java code. This mapping can be found on file `src/test/java/steps/SauceDemoStepDefs.java`.
- Webdriver creation happens at class load stage and it is defined inside `src/test/java/saucedemo/utils/SharedWebDriver`.
- A global configuration file is maintained inside `src/test/resources/production.properties`. On this file, we can find the SauceDemo URL, some credentials of the site, the path of the browser drivers and other stuff.

### Technologies used
- Cucumber Framework: To use BDD notation to define our test cases.
  - Cucumber PicoContainer: To manage object creation and dependency injection. This makes object reusability easier (E.g. Instead of passing the WebDriver object to the page objects via constructor, we rely on the `@Inject` annotation).
- JUnit 4. To define a test runner file that can is used as a starting point for the tests.
- Selenium 3. Library used for interacting with browsers using the WebDriver protocol.
- IntelliJ Idea: This repo is an IntelliJ project. 

### How to run

#### Pre-requisites
- Install JDK 11 ([See here](https://docs.oracle.com/en/java/javase/11/install/overview-jdk-installation.html#GUID-8677A77F-231A-40F7-98B9-1FD0B48C346A))
- Install Maven. ([See here](https://maven.apache.org/install.html))
- Install the latest versions of Mozilla Firefox, Microsoft Edge and Google Chrome.
- For browser automation using Selenium,  download the browser drivers for your environment:
    - Gecko Driver for Firefox. ([link](https://github.com/mozilla/geckodriver/releases))
    - Edge Driver for Internet Explorer / Edge. ([link](https://developer.microsoft.com/en-us/microsoft-edge/tools/webdriver/#downloads))
    - Chrome Driver for Chrome.  ([link](https://chromedriver.chromium.org/))
    
    Once the binaries are downloaded, you should update configuration file `src/test/resources/production.properties` with the paths of the drivers:
  ```
  
  # -----------------------------------------------------
  # WebDriver browser drivers
  # -----------------------------------------------------
  firefox.geckodriver.path=/path/to/geckodriver
  chrome.chromedriver.path=/path/to/chromedriver
  edge.driver.path=drivers/path/to/msedgedriver
  ```
  **NOTE:** This repo contains the necessary drivers to run on Windows 10(x64) on the `/drivers` directory. If you are on Windows, you **MIGHT** be ready to execute the tests. (Drivers are updated periodically) 

####  Execute tests
To execute all the tests/scenarios, open a terminal inside the `SauceDemoUITest` directory and use the following command:
```bash
 mvn test -DtargetBrowser=edge
```

Property `targetBrowser` supports the following values: `chrome`, `firefox`  and `edge`. If the property is not specified or contains an unsupported value, the  framework  will default to `firefox`.

#### Where is my pretty HTML test report?
After `mvn test` finishes, you can find the test reports on the following locations:
- HTML report: `SauceDemoUITests/target/cucumber-reports/index.html`
- JSON report: `SauceDemoUITests/target/cucumber-reports/Cucumber.json`
- XML report: `SauceDemoUITests/target/cucumber-reports/Cucumber.xml`

### Proof of execution.
For your convenience, I have collected sample HTML reports and console outputs for test runs on Chrome, Firefox and Edge. You can find them inside the `SauceDemoUITest/results` directory.
Also, I have uploaded a basic video of the automated tests running against the three browsers. Please find it here:

[![Link to YouTube video](https://img.youtube.com/vi/J4PUtkgYWpA/0.jpg)](https://www.youtube.com/watch?v=J4PUtkgYWpA "Demo Video")