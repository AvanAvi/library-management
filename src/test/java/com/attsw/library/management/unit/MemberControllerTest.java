package com.attsw.library.management.unit;

import com.attsw.library.management.controller.MemberController;
import com.attsw.library.management.entity.Member;
import com.attsw.library.management.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberControllerTest {

    @Mock
    private MemberService memberService;

    private MemberController memberController;

    @BeforeEach
    void setUp() {
        memberController = new MemberController(memberService);
    }

    @Test
    void testSaveMember() {
        // RED 
        Member member = new Member(null, "Avan Avi", "avan.avi@email.com");
        Member savedMember = new Member(1L, "Avan Avi", "avan.avi@email.com");
        
        when(memberService.saveMember(member)).thenReturn(savedMember);
        
        ResponseEntity<Member> response = memberController.saveMember(member);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(savedMember, response.getBody());
        verify(memberService).saveMember(member);
    }
}