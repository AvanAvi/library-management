package com.attsw.library.management.controller;

import com.attsw.library.management.entity.Book;
import com.attsw.library.management.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller  // Avan - @Controller for web pages 
public class BookHTMLController {

    private static final String REDIRECT_BOOKS_WEB = "redirect:/books-web";
    
    private final BookService bookService;

    @Autowired
    public BookHTMLController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books-web")
    public String showBooksPage(Model model) {
        // Get all books from service
        List<Book> books = bookService.findAll();
        
        // Add books to model for Thymeleaf template
        model.addAttribute("books", books);
        
        // Return view name (books.html template)
        return "books";
    }

    @GetMapping("/books-web/new")
    public String showAddBookForm(Model model) {
        // Create empty book object for form binding
        Book book = new Book();
        
        // Add book to model for form
        model.addAttribute("book", book);
        
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
        model.addAttribute("book", book);
        return "edit-book";
    }

    @PostMapping("/books-web/update/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute Book book) {
        book.setId(id);
        bookService.saveBook(book);
        return REDIRECT_BOOKS_WEB;
    }
}