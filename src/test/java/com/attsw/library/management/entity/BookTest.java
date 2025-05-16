package com.attsw.library.management.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void testBookConstructor() {
        // RED - case 
        Book book = new Book();
        assertNotNull(book);
    }
    
}