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
        BookDto bookToCreate = new BookDto(null, "Clean Code", "Robert Martin", "123456789", 2008, "Programming", null);

        ResponseEntity<BookDto> response = restTemplate.postForEntity("/books", bookToCreate, BookDto.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
    }

    @Test
    void testGetBookByIdEndpoint() {
        BookDto bookToCreate = new BookDto(null, "Effective Java", "Joshua Bloch", "987654321", 2017, "Programming", null);
        ResponseEntity<BookDto> createResponse = restTemplate.postForEntity("/books", bookToCreate, BookDto.class);
        Long bookId = createResponse.getBody().getId();

        ResponseEntity<BookDto> getResponse = restTemplate.getForEntity("/books/" + bookId, BookDto.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals(bookId, getResponse.getBody().getId());
    }

    @Test
    void testGetBookByIdWhenNotFound() {
        ResponseEntity<BookDto> response = restTemplate.getForEntity("/books/999", BookDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetAllBooksEndpoint() {
        BookDto book1 = new BookDto(null, "Clean Code", "Robert Martin", "123456789", 2008, "Programming", null);
        BookDto book2 = new BookDto(null, "Effective Java", "Joshua Bloch", "987654321", 2017, "Programming", null);

        restTemplate.postForEntity("/books", book1, BookDto.class);
        restTemplate.postForEntity("/books", book2, BookDto.class);

        ResponseEntity<BookDto[]> response = restTemplate.getForEntity("/books", BookDto[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().length);
    }

    @Test
    void testDeleteBookEndpoint() {
        BookDto bookToCreate = new BookDto(null, "Test Book", "Test Author", "111111111", 2023, "Test", null);
        ResponseEntity<BookDto> createResponse = restTemplate.postForEntity("/books", bookToCreate, BookDto.class);
        Long bookId = createResponse.getBody().getId();

        ResponseEntity<BookDto> getResponse = restTemplate.getForEntity("/books/" + bookId, BookDto.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());

        restTemplate.delete("/books/" + bookId);

        ResponseEntity<BookDto> getDeletedResponse = restTemplate.getForEntity("/books/" + bookId, BookDto.class);
        assertEquals(HttpStatus.NOT_FOUND, getDeletedResponse.getStatusCode());
    }

    @Test
    void testUpdateBookEndpoint() {
        BookDto originalBook = new BookDto(null, "Original Title", "Original Author", "123456789", 2020, "Original", null);
        ResponseEntity<BookDto> createResponse = restTemplate.postForEntity("/books", originalBook, BookDto.class);
        Long bookId = createResponse.getBody().getId();

        BookDto updatedBook = new BookDto(bookId, "Updated Title", "Updated Author", "123456789", 2023, "Updated", null);

        ResponseEntity<BookDto> updateResponse = restTemplate.exchange(
            "/books/" + bookId,
            HttpMethod.PUT,
            new HttpEntity<>(updatedBook),
            BookDto.class
        );

        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertNotNull(updateResponse.getBody());
        assertEquals(bookId, updateResponse.getBody().getId());
    }
}
