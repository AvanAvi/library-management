package com.attsw.library.management.integration;

import com.attsw.library.management.entity.Member;
import com.attsw.library.management.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MemberRepositoryIT {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void testSaveMember() {
        Member member = new Member(null, "Avan Avi", "avan.avi@email.com");
        
        Member savedMember = memberRepository.save(member);
        
        assertNotNull(savedMember.getId());
        assertEquals("Avan Avi", savedMember.getName());
        assertEquals("avan.avi@email.com", savedMember.getEmail());
    }
}