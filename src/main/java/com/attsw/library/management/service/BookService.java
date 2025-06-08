package com.attsw.library.management.service;

import com.attsw.library.management.entity.Book;
import com.attsw.library.management.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.attsw.library.management.exception.BookNotFoundException;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }
    
    public Book findById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> 
          new BookNotFoundException("Book not found with id: " + id));
    }
    
    public List<Book> findAll() {
        return bookRepository.findAll();
    }
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}