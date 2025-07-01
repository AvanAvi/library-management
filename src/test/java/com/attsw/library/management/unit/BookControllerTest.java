package com.attsw.library.management.unit;

import com.attsw.library.management.controller.BookController;
import com.attsw.library.management.entity.Book;
import com.attsw.library.management.dto.BookDto;
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
        BookDto bookDto = new BookDto(null, "Clean Code", "Robert Martin", "123456789", 2008, "Programming", null);
        Book savedBook = new Book(1L, "Clean Code", "Robert Martin", "123456789", 2008, "Programming");
        
        when(bookService.saveBook(any(Book.class))).thenReturn(savedBook);
        
        ResponseEntity<BookDto> response = bookController.saveBook(bookDto);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        BookDto savedBookDto = response.getBody();
        assertNotNull(savedBookDto);
        assertEquals(1L, savedBookDto.getId());
        assertEquals("Clean Code", savedBookDto.getTitle());
        assertEquals("Robert Martin", savedBookDto.getAuthor());
        verify(bookService).saveBook(any(Book.class));
    }
    
    @Test
    void testFindById() {
        Long bookId = 1L;
        Book book = new Book(bookId, "Clean Code", "Robert Martin", "123456789", 2008, "Programming");
        
        when(bookService.findById(bookId)).thenReturn(book);
        
        ResponseEntity<BookDto> response = bookController.findById(bookId);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        BookDto bookDto = response.getBody();
        assertNotNull(bookDto);
        assertEquals(bookId, bookDto.getId());
        assertEquals("Clean Code", bookDto.getTitle());
        assertEquals("Robert Martin", bookDto.getAuthor());
        verify(bookService).findById(bookId);
    }
    
    @Test
    void testFindAll() {
        Book book1 = new Book(1L, "Clean Code", "Robert Martin", "123456789", 2008, "Programming");
        Book book2 = new Book(2L, "Effective Java", "Joshua Bloch", "987654321", 2017, "Programming");
        List<Book> books = Arrays.asList(book1, book2);
        
        when(bookService.findAll()).thenReturn(books);
        
        ResponseEntity<List<BookDto>> response = bookController.findAll();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<BookDto> bookDtos = response.getBody();
        assertNotNull(bookDtos);
        assertEquals(2, bookDtos.size());
        assertEquals("Clean Code", bookDtos.get(0).getTitle());
        assertEquals("Effective Java", bookDtos.get(1).getTitle());
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
        
        when(bookService.findById(nonExistentId))
            .thenThrow(new BookNotFoundException("Book not found with id: " + nonExistentId)); 
        
        ResponseEntity<BookDto> response = bookController.findById(nonExistentId);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(bookService).findById(nonExistentId);
    }
    
    @Test
    void testUpdateBook() {
        Long bookId = 1L;
        BookDto bookDto = new BookDto(bookId, "New Title", "New Author", "123456789", 2021, "New Category", null);
        Book updatedBook = new Book(bookId, "New Title", "New Author", "123456789", 2021, "New Category");
        
        when(bookService.saveBook(any(Book.class))).thenReturn(updatedBook);
        
        ResponseEntity<BookDto> response = bookController.updateBook(bookId, bookDto);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        BookDto updatedBookDto = response.getBody();
        assertNotNull(updatedBookDto);
        assertEquals(bookId, updatedBookDto.getId());
        assertEquals("New Title", updatedBookDto.getTitle());
        assertEquals("New Author", updatedBookDto.getAuthor());
        verify(bookService).saveBook(any(Book.class));
    }
    
    @Test
    void testFindByIdWithBorrowedByMember() {
        
        Long bookId = 1L;
        Long memberId = 2L;
        
        // Create a member who borrowed the book
        com.attsw.library.management.entity.Member borrower = 
            new com.attsw.library.management.entity.Member(memberId, "John Doe", "john@email.com");
        
        // Create a book that is borrowed
        Book book = new Book(bookId, "Borrowed Book", "Test Author", "123456789", 2023, "Test");
        book.setBorrowedBy(borrower);
        
        when(bookService.findById(bookId)).thenReturn(book);
        
        ResponseEntity<BookDto> response = bookController.findById(bookId);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        BookDto bookDto = response.getBody();
        assertNotNull(bookDto);
        assertEquals(bookId, bookDto.getId());
        assertEquals("Borrowed Book", bookDto.getTitle());
        assertEquals(memberId, bookDto.getBorrowedByMemberId()); 
        verify(bookService).findById(bookId);
    }
}