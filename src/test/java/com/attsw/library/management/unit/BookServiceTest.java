package com.attsw.library.management.unit;

import com.attsw.library.management.entity.Book;
import com.attsw.library.management.repository.BookRepository;
import com.attsw.library.management.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.attsw.library.management.exception.BookNotFoundException;

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
    	Long bookId = 1L;
    
    	bookService.deleteBook(bookId);
    
    	verify(bookRepository).deleteById(bookId);
}
    
    @Test
    void testFindByIdThrowsExceptionWhenNotFound() {
       
        Long nonExistentId = 999L;
        
        when(bookRepository.findById(nonExistentId)).thenReturn(Optional.empty());
        
        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> {
            bookService.findById(nonExistentId);
        });
        
        assertEquals("Book not found with id: " + nonExistentId, exception.getMessage());
        verify(bookRepository).findById(nonExistentId);
    }
    
    @Test
    void testUpdateBook() {
        Long bookId = 1L;
        Book existingBook = new Book(bookId, "Title", "Author", "123456789", 2020, "Category");
        Book updateData = new Book(null, "Updated Title", "Updated Author", "123456789", 2023, "Updated");
        Book expectedResult = new Book(bookId, "Updated Title", "Updated Author", "123456789", 2023, "Updated");
        
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(expectedResult);
        
        Book result = bookService.updateBook(bookId, updateData);
        
        assertEquals(expectedResult, result);
        assertEquals(bookId, updateData.getId());
    }
}