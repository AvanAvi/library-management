package com.attsw.library.management.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void testBookConstructor() {
        // RED - The most basic test: Can we create a Book object?
        Book book = new Book();
        assertNotNull(book);
    }
    
}