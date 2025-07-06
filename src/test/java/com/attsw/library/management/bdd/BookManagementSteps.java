package com.attsw.library.management.bdd;

import com.attsw.library.management.entity.Book;
import com.attsw.library.management.service.BookService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookManagementSteps {

    @Autowired
    private BookService bookService;

    private Book book;
    private Book savedBook;
    private List<Book> bookList;

    @Given("I have a book with title {string} and author {string}")
    public void i_have_a_book_with_title_and_author(String title, String author) {
        book = new Book(null, title, author, "123456789", 2023, "Test");
    }

    @When("I save the book")
    public void i_save_the_book() {
        savedBook = bookService.saveBook(book);
    }

    @Then("the book should be saved with an ID")
    public void the_book_should_be_saved_with_an_id() {
        assertNotNull(savedBook);
        assertNotNull(savedBook.getId());
        assertEquals(book.getTitle(), savedBook.getTitle());
        assertEquals(book.getAuthor(), savedBook.getAuthor());
    }

    @Given("a book exists with ID {int}")
    public void a_book_exists_with_id(Integer id) {
        book = new Book(Long.valueOf(id), "Test Book", "Test Author", "123456789", 2023, "Test");
        savedBook = bookService.saveBook(book);
    }

    @When("I search for the book by ID {int}")
    public void i_search_for_the_book_by_id(Integer id) {
        savedBook = bookService.findById(Long.valueOf(id));
    }

    @Then("I should receive the book details")
    public void i_should_receive_the_book_details() {
        assertNotNull(savedBook);
        assertEquals("Test Book", savedBook.getTitle());
        assertEquals("Test Author", savedBook.getAuthor());
    }

    @Given("there are books in the library")
    public void there_are_books_in_the_library() {
        Book book1 = new Book(null, "Book 1", "Author 1", "111111111", 2023, "Test");
        Book book2 = new Book(null, "Book 2", "Author 2", "222222222", 2023, "Test");
        bookService.saveBook(book1);
        bookService.saveBook(book2);
    }

    @When("I request all books")
    public void i_request_all_books() {
        bookList = bookService.findAll();
    }

    @Then("I should receive a list of books")
    public void i_should_receive_a_list_of_books() {
        assertNotNull(bookList);
        assertTrue(bookList.size() >= 2);
    }

    @When("I delete the book with ID {int}")
    public void i_delete_the_book_with_id(Integer id) {
        bookService.deleteBook(Long.valueOf(id));
    }

    @Then("the book should be removed from the library")
    public void the_book_should_be_removed_from_the_library() {
        List<Book> allBooks = bookService.findAll();
        assertTrue(allBooks.stream().noneMatch(b -> b.getId().equals(savedBook.getId())));
    }
}