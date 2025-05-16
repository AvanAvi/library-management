package com.attsw.library.management.service;

import com.attsw.library.management.entity.Book;
import com.attsw.library.management.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

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
    	return bookRepository.findById(id).orElse(null);
    }
    
    public List<Book> findAll() {
        return bookRepository.findAll();
}
}