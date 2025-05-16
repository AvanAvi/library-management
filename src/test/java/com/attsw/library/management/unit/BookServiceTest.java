package com.attsw.library.management.unit;

import com.attsw.library.management.entity.Book;
import com.attsw.library.management.repository.BookRepository;
import com.attsw.library.management.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookService(bookRepository);
    }

    @Test
    void testSaveBook() {
        // RED 
        Book book = new Book(null, "Clean Code", "Robert Martin", "123456789", 2008, "Programming");
        Book savedBook = new Book(1L, "Clean Code", "Robert Martin", "123456789", 2008, "Programming");
        
        when(bookRepository.save(book)).thenReturn(savedBook);
        
        Book result = bookService.saveBook(book);
        
        assertNotNull(result.getId());
        assertEquals("Clean Code", result.getTitle());
        verify(bookRepository).save(book);
    }
}