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
    
 

    @Test
    void testShowBorrowBookForm() throws Exception {
        // RED
        Long bookId = 1L;
        Book book = new Book(bookId, "Test Book", "Test Author", "123456789", 2023, "Test");
        List<Member> members = Arrays.asList(
            new Member(1L, "John Doe", "john@email.com"),
            new Member(2L, "Jane Smith", "jane@email.com")
        );
        
        when(bookService.findById(bookId)).thenReturn(book);
        when(memberService.findAll()).thenReturn(members);
        
        mockMvc.perform(get("/books-web/borrow/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(view().name("borrow-book"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("members"))
                .andExpect(model().attribute("book", book))
                .andExpect(model().attribute("members", members));
        
        verify(bookService).findById(bookId);
        verify(memberService).findAll();
    }

    @Test
    void testBorrowBook() throws Exception {
        // RED
        Long bookId = 1L;
        Long memberId = 2L;
        Book book = new Book(bookId, "Test Book", "Test Author", "123456789", 2023, "Test");
        Member member = new Member(memberId, "John Doe", "john@email.com");
        
        when(bookService.findById(bookId)).thenReturn(book);
        when(memberService.findById(memberId)).thenReturn(member);
        when(bookService.saveBook(any(Book.class))).thenReturn(book);
        
        mockMvc.perform(post("/books-web/borrow/{bookId}", bookId)
                .param("memberId", memberId.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books-web"));
        
        verify(bookService).findById(bookId);
        verify(memberService).findById(memberId);
        verify(bookService).saveBook(book);
    }

    @Test
    void testReturnBook() throws Exception {
        // RED
        Long bookId = 1L;
        Member member = new Member(2L, "John Doe", "john@email.com");
        Book book = new Book(bookId, "Test Book", "Test Author", "123456789", 2023, "Test");
        book.setBorrowedBy(member); // Initially borrowed
        
        when(bookService.findById(bookId)).thenReturn(book);
        when(bookService.saveBook(any(Book.class))).thenReturn(book);
        
        mockMvc.perform(post("/books-web/return/{id}", bookId))
                .andExpected(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books-web"));
        
        verify(bookService).findById(bookId);
        verify(bookService).saveBook(book);
        // Verify the book is no longer borrowed
        assertNull(book.getBorrowedBy());
    }

    @Test
    void testShowBorrowToMemberForm() throws Exception {
        // RED
        Long memberId = 1L;
        Member member = new Member(memberId, "John Doe", "john@email.com");
        List<Book> availableBooks = Arrays.asList(
            new Book(1L, "Available Book 1", "Author 1", "111111111", 2023, "Test"),
            new Book(2L, "Available Book 2", "Author 2", "222222222", 2023, "Test")
        );
        
        when(memberService.findById(memberId)).thenReturn(member);
        when(bookService.findAll()).thenReturn(availableBooks);
        
        mockMvc.perform(get("/books-web/borrow-to/{memberId}", memberId))
                .andExpect(status().isOk())
                .andExpect(view().name("borrow-to-member"))
                .andExpect(model().attributeExists("member"))
                .andExpect(model().attributeExists("availableBooks"))
                .andExpect(model().attribute("member", member));
        
        verify(memberService).findById(memberId);
        verify(bookService).findAll();
    }

    @Test
    void testBorrowBookToMember() throws Exception {
        // RED
        Long memberId = 1L;
        Long bookId = 2L;
        Member member = new Member(memberId, "John Doe", "john@email.com");
        Book book = new Book(bookId, "Test Book", "Test Author", "123456789", 2023, "Test");
        
        when(memberService.findById(memberId)).thenReturn(member);
        when(bookService.findById(bookId)).thenReturn(book);
        when(bookService.saveBook(any(Book.class))).thenReturn(book);
        
        mockMvc.perform(post("/books-web/borrow-to/{memberId}", memberId)
                .param("bookId", bookId.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/members-web"));
        
        verify(memberService).findById(memberId);
        verify(bookService).findById(bookId);
        verify(bookService).saveBook(book);
    }
}