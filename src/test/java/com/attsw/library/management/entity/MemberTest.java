package com.attsw.library.management.entity;

import org.junit.jupiter.api.Test;
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
}