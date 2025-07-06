package com.attsw.library.management.integration;

import com.attsw.library.management.entity.Book;
import com.attsw.library.management.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookRepositoryIT {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void testSaveBook() {
        Book book = new Book(null, "Clean Code", "Robert Martin", "123456789", 2008, "Programming");
        
        Book savedBook = bookRepository.save(book);
        
        assertNotNull(savedBook.getId());
        assertEquals("Clean Code", savedBook.getTitle());
    }
}