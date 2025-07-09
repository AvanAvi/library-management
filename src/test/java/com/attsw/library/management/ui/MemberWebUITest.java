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
class MemberWebUITest {

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
    void testNavigateToMembersPageFromHome() {
        // Navigate to home page
        driver.get(baseUrl + "/");
        
        // Click "Manage Members" button
        WebElement manageMembersButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[@href='/members-web' and contains(text(), 'Manage Members')]")));
        manageMembersButton.click();
        
        wait.until(ExpectedConditions.urlContains("/members-web"));
        
        WebElement membersHeading = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//h2[contains(text(), 'Member Management')]")));
        assertTrue(membersHeading.isDisplayed());
        
        WebElement addMemberButton = driver.findElement(
            By.xpath("//a[@href='/members-web/new' and contains(text(), 'Add New Member')]"));
        assertTrue(addMemberButton.isDisplayed());
    }

    @Test
    void testEmptyMembersListDisplay() {
        // Navigate to members page (should be empty initially)
        driver.get(baseUrl + "/members-web");
        
        // Wait for page to load
        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//h2[contains(text(), 'Member Management')]")));
        
        WebElement emptyMessage = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//h4[contains(text(), 'No Members Found')]")));
        assertTrue(emptyMessage.isDisplayed());
        
        WebElement emptyDescription = driver.findElement(
            By.xpath("//p[contains(text(), 'Your member directory is empty.')]"));
        assertTrue(emptyDescription.isDisplayed());
        
        WebElement addFirstMemberButton = driver.findElement(
            By.xpath("//a[@href='/members-web/new' and contains(text(), 'Add Your First Member')]"));
        assertTrue(addFirstMemberButton.isDisplayed());
    }

    @Test
    void testAddNewMemberFlow() {
        // Navigate to members page
        driver.get(baseUrl + "/members-web");
        
        // Click "Add New Member" button
        WebElement addMemberButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[@href='/members-web/new']")));
        addMemberButton.click();
        
        wait.until(ExpectedConditions.urlContains("/members-web/new"));
        
        WebElement nameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name")));
        WebElement emailField = driver.findElement(By.id("email"));
        
        // Fill out the form
        nameField.sendKeys("Test Member Name");
        emailField.sendKeys("test.member@email.com");
        
        // Submit the form
        WebElement submitButton = driver.findElement(By.xpath("//button[@type='submit' and contains(text(), 'Save Member')]"));
        submitButton.click();
        
        wait.until(ExpectedConditions.urlContains("/members-web"));
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/new")));
        
        WebElement memberName = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//td[contains(text(), 'Test Member Name')]")));
        assertTrue(memberName.isDisplayed());
        
        WebElement memberEmail = driver.findElement(By.xpath("//td[contains(text(), 'test.member@email.com')]"));
        assertTrue(memberEmail.isDisplayed());
    }

    @Test
    void testEditMemberFlow() {
        // First, add a member to edit
        driver.get(baseUrl + "/members-web/new");
        
        // Fill and submit the form
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name"))).sendKeys("Original Member");
        driver.findElement(By.id("email")).sendKeys("original@email.com");
        
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        
        // Wait for redirect to members list
        wait.until(ExpectedConditions.urlContains("/members-web"));
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/new")));
        
        // Click Edit button for the member
        WebElement editButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[contains(@href, '/members-web/edit/') and contains(text(), 'Edit')]")));
        editButton.click();
        
        wait.until(ExpectedConditions.urlContains("/members-web/edit/"));
        
        WebElement nameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name")));
        assertEquals("Original Member", nameField.getAttribute("value"));
        
        WebElement emailField = driver.findElement(By.id("email"));
        assertEquals("original@email.com", emailField.getAttribute("value"));
        
        // Edit the member details
        nameField.clear();
        nameField.sendKeys("Updated Member");
        
        emailField.clear();
        emailField.sendKeys("updated@email.com");
        
        // Submit the update
        WebElement updateButton = driver.findElement(
            By.xpath("//button[@type='submit' and contains(text(), 'Update Member')]"));
        updateButton.click();
        
        wait.until(ExpectedConditions.urlContains("/members-web"));
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/edit")));
        
        WebElement updatedName = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//td[contains(text(), 'Updated Member')]")));
        assertTrue(updatedName.isDisplayed());
        
        WebElement updatedEmail = driver.findElement(By.xpath("//td[contains(text(), 'updated@email.com')]"));
        assertTrue(updatedEmail.isDisplayed());
    }

    @Test
    void testDeleteMemberFlow() {
        // First, add a member to delete
        driver.get(baseUrl + "/members-web/new");
        
        // Fill and submit the form
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name"))).sendKeys("Member to Delete");
        driver.findElement(By.id("email")).sendKeys("delete@email.com");
        
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        
        // Wait for redirect to members list
        wait.until(ExpectedConditions.urlContains("/members-web"));
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/new")));
        
        WebElement memberToDelete = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//td[contains(text(), 'Member to Delete')]")));
        assertTrue(memberToDelete.isDisplayed());
        
        // Click Delete button (will trigger confirmation dialog)
        WebElement deleteButton = driver.findElement(
            By.xpath("//button[@type='submit' and contains(text(), 'Delete')]"));
        deleteButton.click();
        
        // Handle the confirmation dialog
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
        
        // Verify redirect back to members list and member is removed
        wait.until(ExpectedConditions.urlContains("/members-web"));
        
        WebElement emptyMessage = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//h4[contains(text(), 'No Members Found')]")));
        assertTrue(emptyMessage.isDisplayed());
    }

    @Test
    void testFormValidationAndCancelButtons() {
        // Test add form cancel button
        driver.get(baseUrl + "/members-web/new");
        
        WebElement cancelButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[@href='/members-web' and contains(text(), 'Cancel')]")));
        cancelButton.click();
        
        wait.until(ExpectedConditions.urlContains("/members-web"));
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/new")));
        
        // Test form field presence and required attributes
        driver.get(baseUrl + "/members-web/new");
        
        WebElement nameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name")));
        WebElement emailField = driver.findElement(By.id("email"));
        
        assertNotNull(nameField.getAttribute("required"));
        assertNotNull(emailField.getAttribute("required"));
        
        assertEquals("email", emailField.getAttribute("type"));
        
        WebElement nameLabel = driver.findElement(By.xpath("//label[@for='name']"));
        WebElement emailLabel = driver.findElement(By.xpath("//label[@for='email']"));
        
        assertTrue(nameLabel.getText().contains("Name"));
        assertTrue(emailLabel.getText().contains("Email"));
    }

    @Test
    void testEditFormCancelAndPrePopulation() {
        // First, add a member to test edit form
        driver.get(baseUrl + "/members-web/new");
        
        // Fill and submit the form
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name"))).sendKeys("Test Form Member");
        driver.findElement(By.id("email")).sendKeys("form.member@email.com");
        
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        
        // Wait for redirect to members list
        wait.until(ExpectedConditions.urlContains("/members-web"));
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/new")));
        
        // Click Edit button
        WebElement editButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[contains(@href, '/members-web/edit/') and contains(text(), 'Edit')]")));
        editButton.click();
        
        wait.until(ExpectedConditions.urlContains("/members-web/edit/"));
        
        WebElement nameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name")));
        WebElement emailField = driver.findElement(By.id("email"));
        
        assertEquals("Test Form Member", nameField.getAttribute("value"));
        assertEquals("form.member@email.com", emailField.getAttribute("value"));
        
        // Test cancel button on edit form
        WebElement cancelButton = driver.findElement(
            By.xpath("//a[@href='/members-web' and contains(text(), 'Cancel')]"));
        cancelButton.click();
        
        wait.until(ExpectedConditions.urlContains("/members-web"));
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/edit")));
        
        WebElement originalName = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//td[contains(text(), 'Test Form Member')]")));
        assertTrue(originalName.isDisplayed());
        
        WebElement originalEmail = driver.findElement(By.xpath("//td[contains(text(), 'form.member@email.com')]"));
        assertTrue(originalEmail.isDisplayed());
    }
}