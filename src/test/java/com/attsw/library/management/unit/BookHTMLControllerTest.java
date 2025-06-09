package com.attsw.library.management.unit;

import com.attsw.library.management.controller.BookHTMLController;
import com.attsw.library.management.service.BookService;
import com.attsw.library.management.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookHTMLControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    private BookHTMLController bookHTMLController;

    @BeforeEach
    void setUp() {
        bookHTMLController = new BookHTMLController(bookService);
        mockMvc = MockMvcBuilders.standaloneSetup(bookHTMLController).build();
    }

    @Test
    void testShowBooksPage() throws Exception {
        // RED: fail - BookHTMLController doesn't exist yet
        List<Book> books = Arrays.asList(
            new Book(1L, "Clean Code", "Robert Martin", "123456789", 2008, "Programming"),
            new Book(2L, "Effective Java", "Joshua Bloch", "987654321", 2017, "Programming")
        );
        
        when(bookService.findAll()).thenReturn(books);
        
        mockMvc.perform(get("/books-web"))
                .andExpect(status().isOk())
                .andExpect(view().name("books"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", books));
        
        verify(bookService).findAll();
    }

    @Test
    void testShowAddBookForm() throws Exception {
        // RED: Test for displaying add book form
        mockMvc.perform(get("/books-web/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-book"))
                .andExpect(model().attributeExists("book"));
    }

    @Test
    void testSaveBook() throws Exception {
        // RED: Test for saving book via web form
        Book savedBook = new Book(1L, "Clean Code", "Robert Martin", "123456789", 2008, "Programming");
        
        when(bookService.saveBook(any(Book.class))).thenReturn(savedBook);
        
        mockMvc.perform(post("/books-web")
                .param("title", "Clean Code")
                .param("author", "Robert Martin")
                .param("isbn", "123456789")
                .param("publishedYear", "2008")
                .param("category", "Programming"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books-web"));
        
        verify(bookService).saveBook(any(Book.class));
    }

    @Test
    void testDeleteBook() throws Exception {
        // RED: Test for deleting book via web
        Long bookId = 1L;
        
        mockMvc.perform(post("/books-web/delete/{id}", bookId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books-web"));
        
        verify(bookService).deleteBook(bookId);
    }
    
    @Test
    void testShowEditBookForm() throws Exception {
        // ARRANGE
        Long bookId = 1L;
        Book book = new Book(bookId, "Test Book", "Test Author", "123456789", 2023, "Test");
        
        when(bookService.findById(bookId)).thenReturn(book);
        
        // ACT & ASSERT
        mockMvc.perform(get("/books-web/edit/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-book"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attribute("book", book));
        
        verify(bookService).findById(bookId);
    }

    @Test
    void testUpdateBookForm() throws Exception {
        // ARRANGE
        Long bookId = 1L;
        Book savedBook = new Book(bookId, "Updated Book", "Updated Author", "123456789", 2023, "Updated");
        
        when(bookService.saveBook(any(Book.class))).thenReturn(savedBook);
        
        // ACT & ASSERT
        mockMvc.perform(post("/books-web/update/{id}", bookId)
                .param("title", "Updated Book")
                .param("author", "Updated Author")
                .param("isbn", "123456789")
                .param("publishedYear", "2023")
                .param("category", "Updated"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books-web"));
        
        verify(bookService).saveBook(any(Book.class));
    }
}