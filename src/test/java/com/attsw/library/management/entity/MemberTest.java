package com.attsw.library.management.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    @Test
    void testMemberCreation() {
        // RED 
        Member member = new Member();
        assertNotNull(member);
    }
}