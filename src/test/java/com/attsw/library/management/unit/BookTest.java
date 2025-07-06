package com.attsw.library.management.unit;
import com.attsw.library.management.entity.Member;

import org.junit.jupiter.api.Test;

import com.attsw.library.management.entity.Book;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void testBookCreation() {
        Book book = new Book();
        assertNotNull(book);
    }

    @Test
    void testBookWithParameters() {
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

    @Test
    void testBookIsJpaEntity() {
        
        Book book = new Book(1L, "Test Book", "Test Author", "123456789", 2023, "Test");
        
        
        assertTrue(book.getClass().isAnnotationPresent(jakarta.persistence.Entity.class));
        assertNotNull(book.getId());
    }
    
    @Test
    void testBookBorrowedByIsNullByDefault() {
        Book book = new Book(1L, "Clean Code", "Robert Martin", "123456789", 2008, "Programming");
        
        assertNull(book.getBorrowedBy(), "Book should not have a borrower initially");
    }
    
    @Test
    void testBookCanSetBorrowedBy() {
        Book book = new Book(1L, "Clean Code", "Robert Martin", "123456789", 2008, "Programming");
        Member member = new Member(1L, "John Doe", "john@email.com");
        
        book.setBorrowedBy(member);
        
        assertEquals(member, book.getBorrowedBy(), "Book should be borrowed by the member");
    }
    
    @Test
    void testBookIsAvailableWhenNotBorrowed() {
        Book book = new Book(1L, "Clean Code", "Robert Martin", "123456789", 2008, "Programming");
        
        assertTrue(book.isAvailable(), "Book should be available when not borrowed");
    }
    
    @Test
    void testBookIsNotAvailableWhenBorrowed() {
        Book book = new Book(1L, "Clean Code", "Robert Martin", "123456789", 2008, "Programming");
        Member member = new Member(1L, "John Doe", "john@email.com");
        
        book.setBorrowedBy(member);
        
        assertFalse(book.isAvailable(), "Book should not be available when borrowed");
    }
    
    
    
}