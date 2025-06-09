package com.attsw.library.management.controller;

import com.attsw.library.management.entity.Book;
import com.attsw.library.management.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import com.attsw.library.management.exception.BookNotFoundException;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<Book> saveBook(@Valid @RequestBody Book book) {
        Book savedBook = bookService.saveBook(book);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Book> findById(@PathVariable Long id) {
        try {
            Book book = bookService.findById(id);
            return new ResponseEntity<>(book, HttpStatus.OK);
        } catch (BookNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping
    public ResponseEntity<List<Book>> findAll() {
        List<Book> books = bookService.findAll();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody Book book) {
        book.setId(id);
        Book updatedBook = bookService.saveBook(book);
        return new ResponseEntity<>(updatedBook, HttpStatus.OK);
    }
}