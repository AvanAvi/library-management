package com.attsw.library.management.integration;

import com.attsw.library.management.dto.BookDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Testcontainers  
class BookControllerIT {

    @Container  
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")  
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCreateBookEndpoint() {
        // INTEGRATION VALIDATION 
        BookDto bookToCreate = new BookDto(null, "Clean Code", "Robert Martin", "123456789", 2008, "Programming", null);
        
        ResponseEntity<BookDto> response = restTemplate.postForEntity("/books", bookToCreate, BookDto.class);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        BookDto savedBook = response.getBody();
        assertNotNull(savedBook);
        assertNotNull(savedBook.getId());
        assertTrue(savedBook.getId() > 0);
        assertEquals("Clean Code", savedBook.getTitle());
        assertEquals("Robert Martin", savedBook.getAuthor());
        assertEquals("123456789", savedBook.getIsbn());
        assertEquals(2008, savedBook.getPublishedYear());
        assertEquals("Programming", savedBook.getCategory());
    }

    @Test
    void testGetBookByIdEndpoint() {
        // INTEGRATION VALIDATION 
        BookDto bookToCreate = new BookDto(null, "Effective Java", "Joshua Bloch", "987654321", 2017, "Programming", null);
        ResponseEntity<BookDto> createResponse = restTemplate.postForEntity("/books", bookToCreate, BookDto.class);
        Long bookId = createResponse.getBody().getId();
        
        ResponseEntity<BookDto> getResponse = restTemplate.getForEntity("/books/" + bookId, BookDto.class);
        
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        BookDto retrievedBook = getResponse.getBody();
        assertNotNull(retrievedBook);
        assertEquals(bookId, retrievedBook.getId());
        assertEquals("Effective Java", retrievedBook.getTitle());
        assertEquals("Joshua Bloch", retrievedBook.getAuthor());
        assertEquals("987654321", retrievedBook.getIsbn());
        assertEquals(2017, retrievedBook.getPublishedYear());
        assertEquals("Programming", retrievedBook.getCategory());
    }

    @Test
    void testGetBookByIdWhenNotFound() {
        // INTEGRATION VALIDATION 
        ResponseEntity<BookDto> response = restTemplate.getForEntity("/books/999", BookDto.class);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetAllBooksEndpoint() {
        // INTEGRATION VALIDATION 
        // Create multiple books 
        BookDto book1 = new BookDto(null, "Clean Code", "Robert Martin", "123456789", 2008, "Programming", null);
        BookDto book2 = new BookDto(null, "Effective Java", "Joshua Bloch", "987654321", 2017, "Programming", null);
        
        restTemplate.postForEntity("/books", book1, BookDto.class);
        restTemplate.postForEntity("/books", book2, BookDto.class);
        
        // Test GET all books
        ResponseEntity<BookDto[]> response = restTemplate.getForEntity("/books", BookDto[].class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        BookDto[] books = response.getBody();
        assertNotNull(books);
        assertEquals(2, books.length);
        
        // Verify both books are returned 
        boolean foundCleanCode = false;
        boolean foundEffectiveJava = false;
        for (BookDto book : books) {
            if ("Clean Code".equals(book.getTitle())) {
                foundCleanCode = true;
                assertEquals("Robert Martin", book.getAuthor());
                assertEquals("123456789", book.getIsbn());
            } else if ("Effective Java".equals(book.getTitle())) {
                foundEffectiveJava = true;
                assertEquals("Joshua Bloch", book.getAuthor());
                assertEquals("987654321", book.getIsbn());
            }
        }
        assertTrue(foundCleanCode, "Clean Code book should be found");
        assertTrue(foundEffectiveJava, "Effective Java book should be found");
    }

    @Test
    void testDeleteBookEndpoint() {
        // INTEGRATION VALIDATION 
        // Create a book
        BookDto bookToCreate = new BookDto(null, "Test Book", "Test Author", "111111111", 2023, "Test", null);
        ResponseEntity<BookDto> createResponse = restTemplate.postForEntity("/books", bookToCreate, BookDto.class);
        Long bookId = createResponse.getBody().getId();
        
        // Verify book exists
        ResponseEntity<BookDto> getResponse = restTemplate.getForEntity("/books/" + bookId, BookDto.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        
        // Delete the book
        restTemplate.delete("/books/" + bookId);
        
        // Verify book is deleted (return 404)
        ResponseEntity<BookDto> getDeletedResponse = restTemplate.getForEntity("/books/" + bookId, BookDto.class);
        assertEquals(HttpStatus.NOT_FOUND, getDeletedResponse.getStatusCode());
    }
    
    @Test
    void testUpdateBookEndpoint() {
        // INTEGRATION VALIDATION - PUT endpoint
        
        // First create a book
        BookDto originalBook = new BookDto(null, "Original Title", "Original Author", "123456789", 2020, "Original", null);
        ResponseEntity<BookDto> createResponse = restTemplate.postForEntity("/books", originalBook, BookDto.class);
        Long bookId = createResponse.getBody().getId();
        
        // 2nd update the book
        BookDto updatedBook = new BookDto(bookId, "Updated Title", "Updated Author", "123456789", 2023, "Updated", null);
        
        ResponseEntity<BookDto> updateResponse = restTemplate.exchange(
            "/books/" + bookId, 
            HttpMethod.PUT, 
            new HttpEntity<>(updatedBook), 
            BookDto.class
        );
        
        // Verify update response
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        BookDto responseBook = updateResponse.getBody();
        assertNotNull(responseBook);
        assertEquals(bookId, responseBook.getId());
        assertEquals("Updated Title", responseBook.getTitle());
        assertEquals("Updated Author", responseBook.getAuthor());
        assertEquals("123456789", responseBook.getIsbn());
        assertEquals(2023, responseBook.getPublishedYear());
        assertEquals("Updated", responseBook.getCategory());
        
        // Verify the book was actually updated in database
        ResponseEntity<BookDto> getResponse = restTemplate.getForEntity("/books/" + bookId, BookDto.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        BookDto retrievedBook = getResponse.getBody();
        assertEquals("Updated Title", retrievedBook.getTitle());
        assertEquals("Updated Author", retrievedBook.getAuthor());
        assertEquals("Updated", retrievedBook.getCategory());
        assertEquals(2023, retrievedBook.getPublishedYear());
    }
}