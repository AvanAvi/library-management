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
class HomePageUITest {

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
    void testHomePageLoadsSuccessfully() {
        // Navigate to home page
        driver.get(baseUrl + "/");
        
        // Verify page title
        assertEquals("Library Management System", driver.getTitle());
        
        // Verify main heading
        WebElement heading = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//h1[contains(text(), 'Library Management System')]")));
        assertTrue(heading.isDisplayed());
        
        // Verify welcome message
        WebElement welcomeText = driver.findElement(By.className("lead"));
        assertEquals("Welcome to the Library Management System", welcomeText.getText());
    }

    @Test
    void testNavigationToBooks() {
        // Navigate to home page
        driver.get(baseUrl + "/");
        
        // Click "Manage Books" button
        WebElement manageBooksButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[@href='/books-web' and contains(text(), 'Manage Books')]")));
        manageBooksButton.click();
        
        // Verify navigation to books page
        wait.until(ExpectedConditions.urlContains("/books-web"));
        assertTrue(driver.getCurrentUrl().contains("/books-web"));
        
        // Verify books page loaded
        WebElement booksHeading = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//h2[contains(text(), 'Book Management')]")));
        assertTrue(booksHeading.isDisplayed());
    }
    
    @Test
    void testNavigationToMembers() {
        // Navigate to home page
        driver.get(baseUrl + "/");
        
        // Click "Manage Members" button
        WebElement manageMembersButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[@href='/members-web' and contains(text(), 'Manage Members')]")));
        manageMembersButton.click();
        
        // Verify navigation to members page
        wait.until(ExpectedConditions.urlContains("/members-web"));
        assertTrue(driver.getCurrentUrl().contains("/members-web"));
        
        // Verify members page loaded
        WebElement membersHeading = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//h2[contains(text(), 'Member Management')]")));
        assertTrue(membersHeading.isDisplayed());
    }
    
    @Test
    void testNavigationBackToHomeFromBooks() {
        // Navigate to home page
        driver.get(baseUrl + "/");
        
        // Go to books page
        WebElement manageBooksButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[@href='/books-web' and contains(text(), 'Manage Books')]")));
        manageBooksButton.click();
        
        // Wait for books page to load
        wait.until(ExpectedConditions.urlContains("/books-web"));
        
        // Click "Home" button on books page
        WebElement homeButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[@href='/' and contains(text(), 'Home')]")));
        homeButton.click();
        
        // Verify back to home page
        wait.until(ExpectedConditions.urlMatches(".*:"+port+"/$"));
        
        // Verify home page content is loaded
        WebElement heading = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//h1[contains(text(), 'Library Management System')]")));
        assertTrue(heading.isDisplayed());
    }
    
    @Test
    void testNavigationBackToHomeFromMembers() {
        // Navigate to home page
        driver.get(baseUrl + "/");
        
        // Go to members page
        WebElement manageMembersButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[@href='/members-web' and contains(text(), 'Manage Members')]")));
        manageMembersButton.click();
        
        // Wait for members page to load
        wait.until(ExpectedConditions.urlContains("/members-web"));
        
        // Click "Home" button on members page
        WebElement homeButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[@href='/' and contains(text(), 'Home')]")));
        homeButton.click();
        
        // Verify back to home page
        wait.until(ExpectedConditions.urlMatches(".*:"+port+"/$"));
        
        // Verify home page content is loaded
        WebElement heading = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//h1[contains(text(), 'Library Management System')]")));
        assertTrue(heading.isDisplayed());
    }
    
    @Test
    void testHomePageUIElementsPresence() {
        // Navigate to home page
        driver.get(baseUrl + "/");
        
        // Verify card structure
        WebElement mainCard = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.className("card")));
        assertTrue(mainCard.isDisplayed());
        
        // Verify both management cards are present
        WebElement booksCard = driver.findElement(
            By.xpath("//div[@class='card-body']//h5[contains(text(), 'Books')]"));
        assertTrue(booksCard.isDisplayed());
        
        WebElement membersCard = driver.findElement(
            By.xpath("//div[@class='card-body']//h5[contains(text(), 'Members')]"));
        assertTrue(membersCard.isDisplayed());
        
        // Verify card descriptions
        WebElement booksDescription = driver.findElement(
            By.xpath("//p[contains(text(), 'Manage your book collection')]"));
        assertTrue(booksDescription.isDisplayed());
        
        WebElement membersDescription = driver.findElement(
            By.xpath("//p[contains(text(), 'Manage library members')]"));
        assertTrue(membersDescription.isDisplayed());
        
      
        WebElement manageBooksBtn = driver.findElement(
            By.xpath("//a[@href='/books-web' and contains(text(), 'Manage Books')]"));
        assertTrue(manageBooksBtn.isEnabled());
        
        WebElement manageMembersBtn = driver.findElement(
            By.xpath("//a[@href='/members-web' and contains(text(), 'Manage Members')]"));
        assertTrue(manageMembersBtn.isEnabled());
    }
}