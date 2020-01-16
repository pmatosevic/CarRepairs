package org.infiniteam.autoservice;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class SeleniumTest {

    private static final String USER_USERNAME = "user1";
    private static final String USER_PASSWORD = "user1";
    private static final String SERVICE_ADMIN_USERNAME = "boss";
    private static final String SERVICE_ADMIN_PASSWORD = "boss";
    private static String BASE_URL = "http://localhost:8080/";

    WebDriver driver;

    @BeforeEach
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "C:\\Java\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
    }

    @AfterEach
    public void quit() {
        driver.quit();
    }


    private void loginAs(WebDriver driver, String username, String password) {
        driver.get(BASE_URL + "login");
        WebElement element = driver.findElement(By.name("username"));
        element.sendKeys(username);
        element = driver.findElement(By.name("password"));
        element.sendKeys(password);
        driver.findElement(By.id("submit-btn")).click();
        driver.findElement(By.id("auth-dropdown"));         // shown only if the user is logged in
    }

    @Test
    public void loginWithGoodCredentialsWorks() {
        loginAs(driver, USER_USERNAME, USER_PASSWORD);
        assertFalse(driver.findElements(By.id("auth-dropdown")).isEmpty());
    }


    @Test
    public void addingVehicleThatExistsInHuoWorks() {
        loginAs(driver, USER_USERNAME, USER_PASSWORD);

        // Delete other vehicles to start on a clean state
        while (!driver.findElements(By.className("card-header")).isEmpty()) {
            driver.findElement(By.partialLinkText("Detalji")).click();
            driver.findElement(By.id("deleteForm")).submit();
        }

        String vehiclePlate = "ZG 1234 AB";
        driver.findElement(By.id("add-vehicle-btn")).click();
        driver.findElement(By.id("vehiclePlate")).sendKeys(vehiclePlate);
        driver.findElement(By.id("addVehicle")).click();

        // Wait for modal to close
        new WebDriverWait(driver, 1)
                .until(ExpectedConditions.invisibilityOf(driver.findElement(By.id("exampleModal"))));

        String result = driver.findElement(By.className("card-header")).getText();
        assertEquals(vehiclePlate, result);

//        Other way:
//        List<WebElement> elements = driver.findElements(By.className("card-header"));
//        assertTrue(elements.stream()
//                      .map(WebElement::getText)
//                      .anyMatch(text -> text.equals(vehiclePlate)));
    }


    @Test
    public void addingVehicleThatDoesAlreadyExistsFails() {
        loginAs(driver, USER_USERNAME, USER_PASSWORD);

        // Delete other vehicles to start on a clean state
        while (!driver.findElements(By.className("card-header")).isEmpty()) {
            driver.findElement(By.partialLinkText("Detalji")).click();
            driver.findElement(By.id("deleteForm")).submit();
        }

        WebElement modal = driver.findElement(By.id("exampleModal"));
        String vehiclePlate = "ZG 1234 AB";
        driver.findElement(By.id("add-vehicle-btn")).click();
        driver.findElement(By.id("vehiclePlate")).sendKeys(vehiclePlate);
        driver.findElement(By.id("addVehicle")).click();

        // Wait for modal to close
        new WebDriverWait(driver, 1)
                .until(ExpectedConditions.stalenessOf(modal));

        driver.findElement(By.id("add-vehicle-btn")).click();
        driver.findElement(By.id("vehiclePlate")).sendKeys(vehiclePlate);
        driver.findElement(By.id("addVehicle")).click();

        // Assert that error message is displayed
        assertTrue(driver.findElement(By.id("exampleModal")).isDisplayed());
        assertTrue(driver.findElement(By.className("alert")).isDisplayed());
    }


    @Test
    public void addingNewAutoServicePartSuccessfullyAddsItToPriceList() {
        loginAs(driver, SERVICE_ADMIN_USERNAME, SERVICE_ADMIN_PASSWORD);
        driver.get(BASE_URL + "autoservice/priceList");

        // Delete other parts to start from clean state
        while (true) {
            Optional<WebElement> element = driver.findElements(By.className("part-row"))
                    .stream()
                    .findFirst();
            if (element.isEmpty()) break;
            element.get().findElement(By.className("btn-danger")).click();
            new WebDriverWait(driver, 1)
                    .until(ExpectedConditions.stalenessOf(element.get()));
        }

        driver.findElement(By.id("add-part")).click();
        driver.findElement(By.className("modal-content"));

        driver.findElement(By.id("partName")).sendKeys("Guma");
        driver.findElement(By.id("price")).clear();
        driver.findElement(By.id("price")).sendKeys("200");
        driver.findElement(By.id("estimatedDuration")).clear();
        driver.findElement(By.id("estimatedDuration")).sendKeys("100");
        driver.findElement(By.id("save-part")).click();

        new WebDriverWait(driver, 1)
                .until(ExpectedConditions.invisibilityOf(driver.findElement(By.id("modal"))));

        List<WebElement> elems = driver.findElements(By.className("part-row")).stream()
                .filter(e -> e.findElement(By.className("part-name-text")).getText().contains("Guma")
                            && e.findElement(By.className("part-km-text")).getText().contains("100")
                            && e.findElement(By.className("part-price-text")).getText().contains("200"))
                .collect(Collectors.toList());

        assertEquals(1, elems.size());
    }


    @Test
    public void addingNewServiceWorkerAddsNewEmployeeThatIsAbleToLogIn() {
        loginAs(driver, SERVICE_ADMIN_USERNAME, SERVICE_ADMIN_USERNAME);
        driver.findElement(By.id("employees")).click();

        //Delete other employees to start on a clean state
        while (true) {
            Optional<WebElement> element = driver.findElements(By.id("delete-employee"))
                    .stream().filter(WebElement::isEnabled)
                    .findFirst();
            if (element.isEmpty()) break;
            element.get().click();
            new WebDriverWait(driver, 1)
                    .until(ExpectedConditions.invisibilityOf(element.get()));
        }

        driver.findElement(By.id("add-employee")).click();
        driver.findElement(By.id("username")).sendKeys("user2");
        driver.findElement(By.id("password")).sendKeys("user2");
        driver.findElement(By.id("firstName")).sendKeys("Darian");
        driver.findElement(By.id("lastName")).sendKeys("Horvat");
        driver.findElement(By.id("save")).click();

        driver.get(BASE_URL + "logout");

        new WebDriverWait(driver, 1)
                .until(ExpectedConditions.urlToBe(BASE_URL));

        loginAs(driver, "user2", "user2");
        assertFalse(driver.findElements(By.id("auth-dropdown")).isEmpty());     // Employee successfully logged in
    }


    @Test
    public void addingNewServiceWorkerWithUsernameThatExistsDisplaysError() {
        loginAs(driver, SERVICE_ADMIN_USERNAME, SERVICE_ADMIN_USERNAME);
        driver.findElement(By.id("employees")).click();

        //Delete other employees to start on a clean state
        while (true) {
            Optional<WebElement> element = driver.findElements(By.id("delete-employee"))
                    .stream().filter(WebElement::isEnabled)
                    .findFirst();
            if (element.isEmpty()) break;
            element.get().click();
            new WebDriverWait(driver, 1)
                    .until(ExpectedConditions.invisibilityOf(element.get()));
        }

        driver.findElement(By.id("add-employee")).click();
        driver.findElement(By.id("username")).sendKeys("user2");
        driver.findElement(By.id("password")).sendKeys("user2");
        driver.findElement(By.id("firstName")).sendKeys("Darian");
        driver.findElement(By.id("lastName")).sendKeys("Horvat");
        driver.findElement(By.id("save")).click();

        new WebDriverWait(driver, 1)
                .until(ExpectedConditions.invisibilityOf(driver.findElement(By.id("modal"))));

        driver.findElement(By.id("add-employee")).click();
        driver.findElement(By.id("username")).sendKeys("user2");
        driver.findElement(By.id("password")).sendKeys("password");
        driver.findElement(By.id("firstName")).sendKeys("Neki");
        driver.findElement(By.id("lastName")).sendKeys("Novi");
        driver.findElement(By.id("save")).click();

        // Error message is displayed
        assertTrue(driver.findElement(By.className("alert")).isDisplayed());
    }


}
