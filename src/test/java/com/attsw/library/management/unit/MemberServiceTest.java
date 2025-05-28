package com.attsw.library.management.unit;

import com.attsw.library.management.entity.Member;
import com.attsw.library.management.repository.MemberRepository;
import com.attsw.library.management.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberService = new MemberService(memberRepository);
    }

    @Test
    void testSaveMember() {
        // RED 
        Member member = new Member(null, "Avan Avi", "avan.avi@email.com");
        Member savedMember = new Member(1L, "Avan Avi", "avan.avi@email.com");
        
        when(memberRepository.save(member)).thenReturn(savedMember);
        
        Member result = memberService.saveMember(member);
        
        assertNotNull(result.getId());
        assertEquals("Avan Avi", result.getName());
        assertEquals("avan.avi@email.com", result.getEmail());
        verify(memberRepository).save(member);
    }
}