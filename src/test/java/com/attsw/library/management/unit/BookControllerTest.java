package com.attsw.library.management.unit;

import com.attsw.library.management.controller.BookController;
import com.attsw.library.management.entity.Book;
import com.attsw.library.management.service.BookService;
import com.attsw.library.management.exception.BookNotFoundException; 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    private BookController bookController;

    @BeforeEach
    void setUp() {
        bookController = new BookController(bookService);
    }

    @Test
    void testSaveBook() {
        Book book = new Book(null, "Clean Code", "Robert Martin", "123456789", 2008, "Programming");
        Book savedBook = new Book(1L, "Clean Code", "Robert Martin", "123456789", 2008, "Programming");
        
        when(bookService.saveBook(book)).thenReturn(savedBook);
        
        ResponseEntity<Book> response = bookController.saveBook(book);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(savedBook, response.getBody());
        verify(bookService).saveBook(book);
    }
    
    @Test
    void testFindById() {
        Long bookId = 1L;
        Book book = new Book(bookId, "Clean Code", "Robert Martin", "123456789", 2008, "Programming");
        
        when(bookService.findById(bookId)).thenReturn(book);
        
        ResponseEntity<Book> response = bookController.findById(bookId);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(book, response.getBody());
        verify(bookService).findById(bookId);
    }
    
    @Test
    void testFindAll() {
        Book book1 = new Book(1L, "Clean Code", "Robert Martin", "123456789", 2008, "Programming");
        Book book2 = new Book(2L, "Effective Java", "Joshua Bloch", "987654321", 2017, "Programming");
        List<Book> books = Arrays.asList(book1, book2);
        
        when(bookService.findAll()).thenReturn(books);
        
        ResponseEntity<List<Book>> response = bookController.findAll();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(books, response.getBody());
        assertEquals(2, response.getBody().size());
        verify(bookService).findAll();
    }
    
    @Test
    void testDeleteBook() {
        Long bookId = 1L;
        
        ResponseEntity<Void> response = bookController.deleteBook(bookId);
        
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(bookService).deleteBook(bookId);
    }
    
    @Test
    void testFindByIdWhenNotFound() {
        Long nonExistentId = 999L;
        
        // Mock service to throw BookNotFoundException (specific custom exception)
        when(bookService.findById(nonExistentId))
            .thenThrow(new BookNotFoundException("Book not found with id: " + nonExistentId)); // ← CHANGED
        
        ResponseEntity<Book> response = bookController.findById(nonExistentId);
        
        // Assert - Verify exception handling
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(bookService).findById(nonExistentId);
    }
}