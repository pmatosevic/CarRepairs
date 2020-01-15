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

import java.util.concurrent.TimeUnit;

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
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
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
//        new WebDriverWait(driver, 1)
//                .until(ExpectedConditions.or(
//                        ExpectedConditions.presenceOfElementLocated(By.id("auth-dropdown"))
//                        ExpectedConditions.visibilityOf(driver.findElement(By.id("error")))
//                ));
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
            driver.findElement(By.id("deleteVehicle")).click();
        }

        String vehiclePlate = "ZG1234AB";
        driver.findElement(By.id("add-vehicle-btn")).click();
        driver.findElement(By.id("vehiclePlate")).sendKeys(vehiclePlate);
        driver.findElement(By.id("addVehicle")).click();

        // Wait for modal to close
        new WebDriverWait(driver, 1)
                .until(ExpectedConditions.invisibilityOf(driver.findElement(By.id("exampleModal"))));

//        Other way:
//        List<WebElement> elements = driver.findElements(By.className("card-header"));
//        assertTrue(elements.stream().map(WebElement::getText).anyMatch(text -> text.equals(vehiclePlate)));

        String result = driver.findElement(By.className("card-header")).getText();
        assertEquals(vehiclePlate, result);
    }


    @Test
    public void addingVehicleThatDoesAlreadyExistsFails() {
        loginAs(driver, USER_USERNAME, USER_PASSWORD);

        // Delete other vehicles to start on a clean state
        while (!driver.findElements(By.className("card-header")).isEmpty()) {
            driver.findElement(By.partialLinkText("Detalji")).click();
            driver.findElement(By.id("deleteVehicle")).click();
        }

        String vehiclePlate = "ZG1234AB";
        driver.findElement(By.id("add-vehicle-btn")).click();
        driver.findElement(By.id("vehiclePlate")).sendKeys(vehiclePlate);
        driver.findElement(By.id("addVehicle")).click();

        // Wait for modal to close
        new WebDriverWait(driver, 1)
                .until(ExpectedConditions.invisibilityOf(driver.findElement(By.id("exampleModal"))));

        driver.findElement(By.id("add-vehicle-btn")).click();
        driver.findElement(By.id("vehiclePlate")).sendKeys(vehiclePlate);
        driver.findElement(By.id("addVehicle")).click();

        // Assert that error message is displayed
        assertTrue(driver.findElement(By.id("exampleModal")).isDisplayed());
        assertTrue(driver.findElement(By.className("alert")).isDisplayed());
    }


    @Test
    public void addingNewService() {
        loginAs(driver, SERVICE_ADMIN_USERNAME, SERVICE_ADMIN_PASSWORD);

        driver.findElement(By.id("price-nav")).click();
        driver.findElement(By.id("add-service-btn")).click();
        driver.findElement(By.id("partName")).sendKeys("guma");
        driver.findElement(By.id("price")).sendKeys("60");
        driver.findElement(By.id("save-part")).click();
    }


    @Test
    public void addingNewServiceWorker() {
        loginAs(driver, SERVICE_ADMIN_USERNAME, SERVICE_ADMIN_USERNAME);

        driver.findElement(By.id("employees")).click();
        // Delete other vehicles to start on a clean state
        //while (!driver.findElements(By.className("card-header")).isEmpty()) {
            //driver.findElement(By.partialLinkText("Detalji")).click();
            //driver.findElement(By.id("obrisi")).click();

        driver.findElement(By.id("dodaj")).click();
        driver.findElement(By.id("korisnickoime")).click();
        driver.findElement(By.id("korisnickoime")).sendKeys("user2");
        driver.findElement(By.id("pass")).click();
        driver.findElement(By.id("pass")).sendKeys("user2");
        driver.findElement(By.id("name")).click();
        driver.findElement(By.id("name")).sendKeys("Darian");
        driver.findElement(By.id("sur")).click();
        driver.findElement(By.id("sur")).sendKeys("Horvat");
        driver.findElement(By.id("spasi")).click();
        }


    // Iz primjera sa sata

    public void testLoginVehicleOwner() {
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get("http://localhost:8080/login");

        WebElement element = driver.findElement(By.name("username"));
        element.sendKeys("user1");
        element = driver.findElement(By.name("password"));
        element.sendKeys("user1");
        driver.findElement(By.id("submit-btn")).click();

        String title = driver.getTitle();
        assertEquals(title, "Administracija");
        driver.quit();
    }

    public void testLoginBadCreds() {
        WebDriver driver = new ChromeDriver();
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Chrome Driver\\chromedriver.exe");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("http://r2d2.zemris.fer.hr/OPPgrupe/");


        WebElement element = driver.findElement(By.name("username"));
        element.sendKeys("testadmin");

        element = driver.findElement(By.name("pass"));
        element.sendKeys("22345");

        driver.findElement(By.cssSelector("input[type='submit']")).click();

        String title = driver.getTitle();

        assertEquals(title, "OPP - Prijava projektnih grupa");

        driver.quit();

    }


}
