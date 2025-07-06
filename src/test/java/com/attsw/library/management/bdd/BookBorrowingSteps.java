package com.attsw.library.management.bdd;

import com.attsw.library.management.entity.Book;
import com.attsw.library.management.entity.Member;
import com.attsw.library.management.service.BookService;
import com.attsw.library.management.service.MemberService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookBorrowingSteps {

    @Autowired
    private BookService bookService;

    @Autowired
    private MemberService memberService;

    private Book book;
    private Member member;
    private boolean availabilityStatus;

    @Given("a book with ID {int} is available")
    public void a_book_with_id_is_available(Integer bookId) {
        book = new Book(Long.valueOf(bookId), "Test Book", "Test Author", "123456789", 2023, "Test");
        book = bookService.saveBook(book);
        assertTrue(book.isAvailable());
    }

    @Given("a member with ID {int} exists")
    public void a_member_with_id_exists(Integer memberId) {
        member = new Member(Long.valueOf(memberId), "Test Member", "test@email.com");
        member = memberService.saveMember(member);
    }

    @When("the member borrows the book")
    public void the_member_borrows_the_book() {
        book.setBorrowedBy(member);
        book = bookService.saveBook(book);
    }

    @Then("the book should be marked as borrowed by the member")
    public void the_book_should_be_marked_as_borrowed_by_the_member() {
        assertNotNull(book.getBorrowedBy());
        assertEquals(member.getId(), book.getBorrowedBy().getId());
        assertFalse(book.isAvailable());
    }

    @Given("a book with ID {int} is borrowed by member with ID {int}")
    public void a_book_with_id_is_borrowed_by_member_with_id(Integer bookId, Integer memberId) {
        book = new Book(Long.valueOf(bookId), "Test Book", "Test Author", "123456789", 2023, "Test");
        member = new Member(Long.valueOf(memberId), "Test Member", "test@email.com");
        
        member = memberService.saveMember(member);
        book.setBorrowedBy(member);
        book = bookService.saveBook(book);
    }

    @When("the member returns the book")
    public void the_member_returns_the_book() {
        book.setBorrowedBy(null);
        book = bookService.saveBook(book);
    }

    @Then("the book should be marked as available")
    public void the_book_should_be_marked_as_available() {
        assertNull(book.getBorrowedBy());
        assertTrue(book.isAvailable());
    }

    @Given("a book with ID {int} exists")
    public void a_book_with_id_exists(Integer bookId) {
        book = new Book(Long.valueOf(bookId), "Test Book", "Test Author", "123456789", 2023, "Test");
        book = bookService.saveBook(book);
    }

    @When("I check if the book is available")
    public void i_check_if_the_book_is_available() {
        availabilityStatus = book.isAvailable();
    }

    @Then("I should get the availability status")
    public void i_should_get_the_availability_status() {
        assertNotNull(availabilityStatus);
        assertTrue(availabilityStatus);
    }
}