package com.attsw.library.management.unit;

import org.junit.jupiter.api.Test;

import com.attsw.library.management.entity.Member;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    @Test
    void testMemberCreation() {
        // GREEN 
        Member member = new Member();
        assertNotNull(member);
    }

    @Test
    void testMemberWithNameAndEmail() {
        // GREEN 
        Member member = new Member(1L, "Avan Avi", "avan.avi@email.com");
        
        assertEquals(1L, member.getId());
        assertEquals("Avan Avi", member.getName());
        assertEquals("avan.avi@email.com", member.getEmail());
    }

    @Test
    void testMemberIsJpaEntity() {
        // RED 
        Member member = new Member(1L, "Avan Avi", "avan.avi@email.com");
        
      
        assertTrue(member.getClass().isAnnotationPresent(jakarta.persistence.Entity.class));
        assertNotNull(member.getId());
    }
    
 

    @Test
    void testMemberHasBorrowedBooksListInitially() {
        // RED
        Member member = new Member(1L, "John Doe", "john@email.com");
        
        java.util.List<com.attsw.library.management.entity.Book> borrowedBooks = member.getBorrowedBooks();
        assertNotNull(borrowedBooks, "Borrowed books list should not be null");
        assertTrue(borrowedBooks.isEmpty(), "Borrowed books list should be empty initially");
    }
}