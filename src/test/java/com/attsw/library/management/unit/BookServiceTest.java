package com.attsw.library.management.unit;

import com.attsw.library.management.entity.Book;
import com.attsw.library.management.repository.BookRepository;
import com.attsw.library.management.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Arrays;
import java.util.List;

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
        // GREEN 
        Book book = new Book(null, "Clean Code", "Robert Martin", "123456789", 2008, "Programming");
        Book savedBook = new Book(1L, "Clean Code", "Robert Martin", "123456789", 2008, "Programming");
        
        when(bookRepository.save(book)).thenReturn(savedBook);
        
        Book result = bookService.saveBook(book);
        
        assertNotNull(result.getId());
        assertEquals("Clean Code", result.getTitle());
        verify(bookRepository).save(book);
    }

    @Test
    void testFindById() {
        // RED

        Long bookId = 1L;
        Book book = new Book(bookId, "Clean Code", "Robert Martin", "123456789", 2008, "Programming");
        
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        
        Book result = bookService.findById(bookId);
        
        assertNotNull(result);
        assertEquals(bookId, result.getId());
        assertEquals("Clean Code", result.getTitle());
        verify(bookRepository).findById(bookId);
    }
    

    @Test
    void testFindAll() {
    // RED 
    	Book book1 = new Book(1L, "Clean Code", "Robert Martin", "123456789", 2008, "Programming");
    	Book book2 = new Book(2L, "Effective Java", "Joshua Bloch", "987654321", 2017, "Programming");
    	List<Book> books = Arrays.asList(book1, book2);
    
    	when(bookRepository.findAll()).thenReturn(books);
    
    	List<Book> result = bookService.findAll();
    
	    assertNotNull(result);
	    assertEquals(2, result.size());
	    assertEquals("Clean Code", result.get(0).getTitle());
	    assertEquals("Effective Java", result.get(1).getTitle());
	    verify(bookRepository).findAll();
}
    

    @Test
    void testDeleteBook() {
    // RED 
    	Long bookId = 1L;
    
    	bookService.deleteBook(bookId);
    
    	verify(bookRepository).deleteById(bookId);
}
}