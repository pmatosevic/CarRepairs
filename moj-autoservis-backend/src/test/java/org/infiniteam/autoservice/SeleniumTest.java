package org.infiniteam.autoservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class SeleniumTest {

    private static final String USER_USERNAME = "user1";
    private static final String USER_PASSWORD = "user1";
    private static String BASE_URL = "http://localhost:8080/";

    @BeforeEach
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "C:\\Java\\chromedriver.exe");
    }

    private WebDriver newDriver() {
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        return driver;
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
        WebDriver driver = newDriver();
        loginAs(driver, USER_USERNAME, USER_PASSWORD);
        assertFalse(driver.findElements(By.id("auth-dropdown")).isEmpty());
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
