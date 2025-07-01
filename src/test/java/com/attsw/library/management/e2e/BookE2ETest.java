package com.attsw.library.management.e2e;

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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Testcontainers  
class BookE2ETest {

    @Container  
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")  
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCompleteBookWorkflow() {
        BookDto newBook = new BookDto(null, "Test Book E2E", "Test Author", "E2E-123456", 2023, "Testing", null);
        
        ResponseEntity<BookDto> createResponse = restTemplate.postForEntity("/books", newBook, BookDto.class);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
    }

    @Test 
    void testBookValidationFailure() {
        
        BookDto invalidBook = new BookDto(null, null, null, null, null, null, null); 
        
        ResponseEntity<BookDto> response = restTemplate.postForEntity("/books", invalidBook, BookDto.class);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}