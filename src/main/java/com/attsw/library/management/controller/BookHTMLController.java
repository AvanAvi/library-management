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
        List<Book> books = bookService.findAll();
        model.addAttribute(BOOKS_ATTRIBUTE, books);
        return BOOKS_ATTRIBUTE;
    }

    @GetMapping("/books-web/new")
    public String showAddBookForm(Model model) {
        Book book = new Book();
        model.addAttribute(BOOK_ATTRIBUTE, book);
        return "add-book";
    }

    @PostMapping("/books-web")
    public String saveBook(@ModelAttribute Book book) {
        bookService.saveBook(book);
        return REDIRECT_BOOKS_WEB;
    }

    @PostMapping("/books-web/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
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
        Book book = bookService.findById(id);
        List<Member> members = memberService.findAll();
        model.addAttribute(BOOK_ATTRIBUTE, book);
        model.addAttribute("members", members);
        return "borrow-book";
    }
    
    @PostMapping("/books-web/borrow/{bookId}")
    public String borrowBook(@PathVariable Long bookId, @RequestParam Long memberId) {
        Book book = bookService.findById(bookId);
        Member member = memberService.findById(memberId);
        book.setBorrowedBy(member);
        bookService.saveBook(book);
        return REDIRECT_BOOKS_WEB;
    }
    
    @PostMapping("/books-web/return/{id}")
    public String returnBook(@PathVariable Long id) {
        Book book = bookService.findById(id);
        book.setBorrowedBy(null);
        bookService.saveBook(book);
        return REDIRECT_BOOKS_WEB;
    }
    
    @GetMapping("/books-web/borrow-to/{memberId}")
    @Transactional(readOnly = true)
    public String showBorrowToMemberForm(@PathVariable Long memberId, Model model) {
        Member member = memberService.findById(memberId);
        // Force initialization of lazy collection within transaction
        int borrowedBooksCount = member.getBorrowedBooks().size();
        
        List<Book> availableBooks = bookService.findAll().stream()
                .filter(Book::isAvailable)
                .toList();
        
        model.addAttribute("member", member);
        model.addAttribute("availableBooks", availableBooks);
        model.addAttribute("borrowedBooksCount", borrowedBooksCount);
        
        return "borrow-to-member";
    }
    
    @PostMapping("/books-web/borrow-to/{memberId}")
    public String borrowBookToMember(@PathVariable Long memberId, @RequestParam Long bookId) {
        Book book = bookService.findById(bookId);
        Member member = memberService.findById(memberId);
        book.setBorrowedBy(member);
        bookService.saveBook(book);
        return "redirect:/members-web";
    }
}