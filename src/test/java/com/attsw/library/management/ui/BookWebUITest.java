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
    
    @Test
    void testAddNewBookFlow() {
        // Navigate to books page
        driver.get(baseUrl + "/books-web");
        
        // Click "Add New Book" button
        WebElement addBookButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[@href='/books-web/new']")));
        addBookButton.click();
        
        // Verify we're on add book page
        wait.until(ExpectedConditions.urlContains("/books-web/new"));
        
        // Verify add book form elements
        WebElement titleField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("title")));
        WebElement authorField = driver.findElement(By.id("author"));
        WebElement isbnField = driver.findElement(By.id("isbn"));
        WebElement yearField = driver.findElement(By.id("publishedYear"));
        WebElement categoryField = driver.findElement(By.id("category"));
        
        // Fill out the form
        titleField.sendKeys("Test Book Title");
        authorField.sendKeys("Test Author");
        isbnField.sendKeys("TEST-123456789");
        yearField.sendKeys("2023");
        categoryField.sendKeys("Testing");
        
        // Submit the form
        WebElement submitButton = driver.findElement(By.xpath("//button[@type='submit' and contains(text(), 'Save Book')]"));
        submitButton.click();
        
        // Verify redirect to books list
        wait.until(ExpectedConditions.urlContains("/books-web"));
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/new")));
        
        // Verify the book appears in the list
        WebElement bookTitle = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//td[contains(text(), 'Test Book Title')]")));
        assertTrue(bookTitle.isDisplayed());
        
        // Verify other book details are displayed
        WebElement bookAuthor = driver.findElement(By.xpath("//td[contains(text(), 'Test Author')]"));
        WebElement bookIsbn = driver.findElement(By.xpath("//td[contains(text(), 'TEST-123456789')]"));
        assertTrue(bookAuthor.isDisplayed());
        assertTrue(bookIsbn.isDisplayed());
    }
    
    @Test
    void testEditBookFlow() {
        // First, add a book to edit
        driver.get(baseUrl + "/books-web/new");
        
        // Fill and submit the form
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("title"))).sendKeys("Original Title");
        driver.findElement(By.id("author")).sendKeys("Original Author");
        driver.findElement(By.id("isbn")).sendKeys("ORIG-123456789");
        driver.findElement(By.id("publishedYear")).sendKeys("2020");
        driver.findElement(By.id("category")).sendKeys("Original");
        
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        
        // Wait for redirect to books list
        wait.until(ExpectedConditions.urlContains("/books-web"));
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/new")));
        
        // Click Edit button for the book
        WebElement editButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[contains(@href, '/books-web/edit/') and contains(text(), 'Edit')]")));
        editButton.click();
        
        // Verify we're on edit page
        wait.until(ExpectedConditions.urlContains("/books-web/edit/"));
        
        // Verify form is pre-filled
        WebElement titleField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("title")));
        assertEquals("Original Title", titleField.getAttribute("value"));
        
        // Edit the book details
        titleField.clear();
        titleField.sendKeys("Updated Title");
        
        WebElement authorField = driver.findElement(By.id("author"));
        authorField.clear();
        authorField.sendKeys("Updated Author");
        
        // Submit the update
        WebElement updateButton = driver.findElement(
            By.xpath("//button[@type='submit' and contains(text(), 'Update Book')]"));
        updateButton.click();
        
        // Verify redirect to books list
        wait.until(ExpectedConditions.urlContains("/books-web"));
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/edit")));
        
        // Verify updated book appears in list
        WebElement updatedTitle = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//td[contains(text(), 'Updated Title')]")));
        assertTrue(updatedTitle.isDisplayed());
        
        WebElement updatedAuthor = driver.findElement(By.xpath("//td[contains(text(), 'Updated Author')]"));
        assertTrue(updatedAuthor.isDisplayed());
    }
    
    @Test
    void testDeleteBookFlow() {
        // First, add a book to delete
        driver.get(baseUrl + "/books-web/new");
        
        // Fill and submit the form
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("title"))).sendKeys("Book to Delete");
        driver.findElement(By.id("author")).sendKeys("Delete Author");
        driver.findElement(By.id("isbn")).sendKeys("DEL-123456789");
        driver.findElement(By.id("publishedYear")).sendKeys("2021");
        driver.findElement(By.id("category")).sendKeys("Delete");
        
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        
        // Wait for redirect to books list
        wait.until(ExpectedConditions.urlContains("/books-web"));
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/new")));
        
        // Verify book is in the list
        WebElement bookToDelete = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//td[contains(text(), 'Book to Delete')]")));
        assertTrue(bookToDelete.isDisplayed());
        
        // Click Delete button (will trigger confirmation dialog)
        WebElement deleteButton = driver.findElement(
            By.xpath("//button[@type='submit' and contains(text(), 'Delete')]"));
        deleteButton.click();
        
        // Handle the confirmation dialog
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
        
        // Verify redirect back to books list and book is removed
        wait.until(ExpectedConditions.urlContains("/books-web"));
        
        // Verify book is no longer in the list (should show empty state)
        WebElement emptyMessage = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//h4[contains(text(), 'No Books Found')]")));
        assertTrue(emptyMessage.isDisplayed());
    }
}