package com.attsw.library.management.ui;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Testcontainers
class BookWebUITest {

    @Container
    static BrowserWebDriverContainer<?> chrome = new BrowserWebDriverContainer<>("selenium/standalone-chrome:4.15.0")
            .withCapabilities(new ChromeOptions());

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private WebDriverWait wait;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        driver = new RemoteWebDriver(chrome.getSeleniumAddress(), new ChromeOptions());
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        baseUrl = "http://host.docker.internal:" + port;
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testNavigateToBooksPageFromHome() {
        // Navigate to home page
        driver.get(baseUrl + "/");
        
        // Click "Manage Books" button
        WebElement manageBooksButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[@href='/books-web' and contains(text(), 'Manage Books')]")));
        manageBooksButton.click();
        
        // Verify we're on books page
        wait.until(ExpectedConditions.urlContains("/books-web"));
        
        // Verify books page content
        WebElement booksHeading = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//h2[contains(text(), 'Book Management')]")));
        assertTrue(booksHeading.isDisplayed());
        
        // Verify "Add New Book" button is present
        WebElement addBookButton = driver.findElement(
            By.xpath("//a[@href='/books-web/new' and contains(text(), 'Add New Book')]"));
        assertTrue(addBookButton.isDisplayed());
    }

    @Test
    void testEmptyBooksListDisplay() {
        // Navigate to books page (should be empty initially)
        driver.get(baseUrl + "/books-web");
        
        // Wait for page to load
        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//h2[contains(text(), 'Book Management')]")));
        
        // Verify empty state message
        WebElement emptyMessage = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//h4[contains(text(), 'No Books Found')]")));
        assertTrue(emptyMessage.isDisplayed());
        
        // Verify empty state description
        WebElement emptyDescription = driver.findElement(
            By.xpath("//p[contains(text(), 'Your library collection is empty.')]"));
        assertTrue(emptyDescription.isDisplayed());
        
        // Verify "Add Your First Book" button
        WebElement addFirstBookButton = driver.findElement(
            By.xpath("//a[@href='/books-web/new' and contains(text(), 'Add Your First Book')]"));
        assertTrue(addFirstBookButton.isDisplayed());
    }
}