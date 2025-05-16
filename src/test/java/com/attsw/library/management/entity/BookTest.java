package com.attsw.library.management.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void testBookCreation() {
        // GREEN 
        Book book = new Book();
        assertNotNull(book);
    }

    @Test
    void testBookWithParameters() {
        // GREEN 
        Book book = new Book(1L, "Clean Code", "Robert C. Martin", "978-0132350884", 2008, "Programming");
        
        assertEquals(1L, book.getId());
        assertEquals("Clean Code", book.getTitle());
        assertEquals("Robert C. Martin", book.getAuthor());
        assertEquals("978-0132350884", book.getIsbn());
        assertEquals(2008, book.getPublishedYear());
        assertEquals("Programming", book.getCategory());
    }

    @Test
    void testBookGettersAndSetters() {
        // GREEN 
        Book book = new Book();
        
        book.setId(2L);
        book.setTitle("Effective Java");
        book.setAuthor("Joshua Bloch");
        book.setIsbn("978-0134685991");
        book.setPublishedYear(2017);
        book.setCategory("Programming");
        
        assertEquals(2L, book.getId());
        assertEquals("Effective Java", book.getTitle());
        assertEquals("Joshua Bloch", book.getAuthor());
        assertEquals("978-0134685991", book.getIsbn());
        assertEquals(2017, book.getPublishedYear());
        assertEquals("Programming", book.getCategory());
    }

    // RED - New test for JPA annotations 
    @Test
    void testBookIsJpaEntity() {
        
        Book book = new Book(1L, "Test Book", "Test Author", "123456789", 2023, "Test");
        
        // We'll verify JPA annotations are present (this will fail until we add them)
        assertTrue(book.getClass().isAnnotationPresent(jakarta.persistence.Entity.class));
        assertNotNull(book.getId());
    }
}