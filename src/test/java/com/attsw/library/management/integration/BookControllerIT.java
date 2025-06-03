package com.attsw.library.management.integration;

import com.attsw.library.management.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCreateBookEndpoint() {
        // INTEGRATION VALIDATION
        Book bookToCreate = new Book(null, "Clean Code", "Robert Martin", "123456789", 2008, "Programming");
        
        ResponseEntity<Book> response = restTemplate.postForEntity("/books", bookToCreate, Book.class);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Book savedBook = response.getBody();
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
        Book bookToCreate = new Book(null, "Effective Java", "Joshua Bloch", "987654321", 2017, "Programming");
        ResponseEntity<Book> createResponse = restTemplate.postForEntity("/books", bookToCreate, Book.class);
        Long bookId = createResponse.getBody().getId();
        
        ResponseEntity<Book> getResponse = restTemplate.getForEntity("/books/" + bookId, Book.class);
        
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        Book retrievedBook = getResponse.getBody();
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
        
        ResponseEntity<Book> response = restTemplate.getForEntity("/books/999", Book.class);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}