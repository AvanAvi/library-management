package com.attsw.library.management.controller;

import com.attsw.library.management.entity.Book;
import com.attsw.library.management.dto.BookDto;
import com.attsw.library.management.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import com.attsw.library.management.exception.BookNotFoundException;

@RestController
@RequestMapping("/books")
@Transactional
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<BookDto> saveBook(@Valid @RequestBody BookDto bookDto) {
        Book book = convertToEntity(bookDto);
        Book savedBook = bookService.saveBook(book);
        BookDto savedBookDto = convertToDto(savedBook);
        return new ResponseEntity<>(savedBookDto, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BookDto> findById(@PathVariable Long id) {
        try {
            Book book = bookService.findById(id);
            BookDto bookDto = convertToDto(book);
            return new ResponseEntity<>(bookDto, HttpStatus.OK);
        } catch (BookNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping
    public ResponseEntity<List<BookDto>> findAll() {
        List<Book> books = bookService.findAll();
        List<BookDto> bookDtos = books.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(bookDtos, HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id, @Valid @RequestBody BookDto bookDto) {
        bookDto.setId(id);
        Book book = convertToEntity(bookDto);
        Book updatedBook = bookService.saveBook(book);
        BookDto updatedBookDto = convertToDto(updatedBook);
        return new ResponseEntity<>(updatedBookDto, HttpStatus.OK);
    }
    
    // Entity to DTO conversion
    private BookDto convertToDto(Book book) {
        Long borrowedByMemberId = book.getBorrowedBy() != null ? book.getBorrowedBy().getId() : null;
        
        return new BookDto(
            book.getId(),
            book.getTitle(),
            book.getAuthor(),
            book.getIsbn(),
            book.getPublishedYear(),
            book.getCategory(),
            borrowedByMemberId
        );
    }
    
    // DTO to Entity conversion
    private Book convertToEntity(BookDto bookDto) {
        Book book = new Book(
            bookDto.getId(),
            bookDto.getTitle(),
            bookDto.getAuthor(),
            bookDto.getIsbn(),
            bookDto.getPublishedYear(),
            bookDto.getCategory()
        );
        
        
        
        return book;
    }
}