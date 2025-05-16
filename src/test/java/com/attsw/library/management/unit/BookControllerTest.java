package com.attsw.library.management.unit;

import com.attsw.library.management.controller.BookController;
import com.attsw.library.management.entity.Book;
import com.attsw.library.management.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        // RED 
        Book book = new Book(null, "Clean Code", "Robert Martin", "123456789", 2008, "Programming");
        Book savedBook = new Book(1L, "Clean Code", "Robert Martin", "123456789", 2008, "Programming");
        
        when(bookService.saveBook(book)).thenReturn(savedBook);
        
        ResponseEntity<Book> response = bookController.saveBook(book);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(savedBook, response.getBody());
        verify(bookService).saveBook(book);
    }
}