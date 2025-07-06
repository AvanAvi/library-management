package com.attsw.library.management.controller;

import com.attsw.library.management.entity.Book;
import com.attsw.library.management.entity.Member;
import com.attsw.library.management.service.BookService;
import com.attsw.library.management.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Controller  
public class BookHTMLController {

    private static final String REDIRECT_BOOKS_WEB = "redirect:/books-web";
    private static final String BOOK_ATTRIBUTE = "book";
    private static final String BOOKS_ATTRIBUTE = "books";
    
    private final BookService bookService;
    private final MemberService memberService;

    @Autowired
    public BookHTMLController(BookService bookService, MemberService memberService) {
        this.bookService = bookService;
        this.memberService = memberService;
    }

    @GetMapping("/books-web")
    @Transactional(readOnly = true)
    public String showBooksPage(Model model) {
        // Get all books from service
        List<Book> books = bookService.findAll();
        
        // Add books to model for Thymeleaf template
        model.addAttribute(BOOKS_ATTRIBUTE, books);
        
        // Return view name (books.html template)
        return "books";
    }

    @GetMapping("/books-web/new")
    public String showAddBookForm(Model model) {
        // Create empty book object for form binding
        Book book = new Book();
        
        // Add book to model for form
        model.addAttribute(BOOK_ATTRIBUTE, book);
        
        // Return add book form view
        return "add-book";
    }

    @PostMapping("/books-web")
    public String saveBook(@ModelAttribute Book book) {
        // Save book using service
        bookService.saveBook(book);
        
        // Redirect to books list (POST-Redirect-GET pattern)
        return REDIRECT_BOOKS_WEB;
    }

    @PostMapping("/books-web/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        // Delete book using service
        bookService.deleteBook(id);
        
        // Redirect to books list
        return REDIRECT_BOOKS_WEB;
    }
    
    @GetMapping("/books-web/edit/{id}")
    public String showEditBookForm(@PathVariable Long id, Model model) {
        Book book = bookService.findById(id);
        model.addAttribute(BOOK_ATTRIBUTE, book);
        return "edit-book";
    }

    @PostMapping("/books-web/update/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute Book book) {
        book.setId(id);
        bookService.saveBook(book);
        return REDIRECT_BOOKS_WEB;
    }
    
    
    
    @GetMapping("/books-web/borrow/{id}")
    public String showBorrowBookForm(@PathVariable Long id, Model model) {
        // Get the book to be borrowed
        Book book = bookService.findById(id);
        
        // Get all members for selection
        List<Member> members = memberService.findAll();
        
        // Add to model
        model.addAttribute(BOOK_ATTRIBUTE, book);
        model.addAttribute("members", members);
        
        return "borrow-book";
    }
    
    @PostMapping("/books-web/borrow/{bookId}")
    public String borrowBook(@PathVariable Long bookId, @RequestParam Long memberId) {
        // Get book and member
        Book book = bookService.findById(bookId);
        Member member = memberService.findById(memberId);
        
        // Set the borrowing relationship
        book.setBorrowedBy(member);
        
        // Save the book with updated relationship
        bookService.saveBook(book);
        
        return REDIRECT_BOOKS_WEB;
    }
    
    @PostMapping("/books-web/return/{id}")
    public String returnBook(@PathVariable Long id) {
        // Get the book
        Book book = bookService.findById(id);
        
        // Clear the borrowing relationship
        book.setBorrowedBy(null);
        
        // Save the book
        bookService.saveBook(book);
        
        return REDIRECT_BOOKS_WEB;
    }
    
    @GetMapping("/books-web/borrow-to/{memberId}")
    @Transactional(readOnly = true)
    public String showBorrowToMemberForm(@PathVariable Long memberId, Model model) {
        // Get the member
        Member member = memberService.findById(memberId);
        // Force initialization of lazy collection within transaction
        int borrowedBooksCount = member.getBorrowedBooks().size();
        
        // Get all available books
        List<Book> availableBooks = bookService.findAll().stream()
                .filter(Book::isAvailable)
                .toList();
        
        // Add to model
        model.addAttribute("member", member);
        model.addAttribute("availableBooks", availableBooks);
        model.addAttribute("borrowedBooksCount", borrowedBooksCount);
        
        return "borrow-to-member";
    }
    
    @PostMapping("/books-web/borrow-to/{memberId}")
    public String borrowBookToMember(@PathVariable Long memberId, @RequestParam Long bookId) {
        // Get book and member
        Book book = bookService.findById(bookId);
        Member member = memberService.findById(memberId);
        
        // Set the borrowing relationship
        book.setBorrowedBy(member);
        
        // Save the book
        bookService.saveBook(book);
        
        return "redirect:/members-web";
    }
}