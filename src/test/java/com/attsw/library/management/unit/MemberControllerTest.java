package com.attsw.library.management.unit;

import com.attsw.library.management.controller.MemberController;
import com.attsw.library.management.entity.Member;
import com.attsw.library.management.service.MemberService;
import com.attsw.library.management.exception.MemberNotFoundException; 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;

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
        Member member = new Member(null, "Avan Avi", "avan.avi@email.com");
        Member savedMember = new Member(1L, "Avan Avi", "avan.avi@email.com");
        
        when(memberService.saveMember(member)).thenReturn(savedMember);
        
        ResponseEntity<Member> response = memberController.saveMember(member);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(savedMember, response.getBody());
        verify(memberService).saveMember(member);
    }

    @Test
    void testFindById() {
        Long memberId = 1L;
        Member member = new Member(memberId, "Avan Avi", "avan.avi@email.com");
        
        when(memberService.findById(memberId)).thenReturn(member);
        
        ResponseEntity<Member> response = memberController.findById(memberId);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(member, response.getBody());
        verify(memberService).findById(memberId);
    }
    
    @Test
    void testFindAll() {
        Member member1 = new Member(1L, "Avan Avi", "avan.avi@email.com");
        Member member2 = new Member(2L, "John Doe", "john.doe@email.com");
        List<Member> members = Arrays.asList(member1, member2);
        
        when(memberService.findAll()).thenReturn(members);
        
        ResponseEntity<List<Member>> response = memberController.findAll();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(members, response.getBody());
        assertEquals(2, response.getBody().size());
        verify(memberService).findAll();
    }
    
    @Test
    void testDeleteMember() {
        Long memberId = 1L;
        
        ResponseEntity<Void> response = memberController.deleteMember(memberId);
        
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(memberService).deleteMember(memberId);
    }
    
    @Test
    void testFindByIdWhenNotFound() {
        Long nonExistentId = 999L;
        
        when(memberService.findById(nonExistentId))
            .thenThrow(new MemberNotFoundException("Member not found with id: " + nonExistentId)); // ← CHANGED

        ResponseEntity<Member> response = memberController.findById(nonExistentId);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(memberService).findById(nonExistentId);
    }
    
    @Test
    void testUpdateMember() {
        // ARRANGE
        Long memberId = 1L;
        Member updatedMember = new Member(memberId, "New Name", "new@email.com");
        
        when(memberService.saveMember(any(Member.class))).thenReturn(updatedMember);
        
        // ACT
        ResponseEntity<Member> response = memberController.updateMember(memberId, updatedMember);
        
        // ASSERT
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedMember, response.getBody());
        assertEquals(memberId, response.getBody().getId());
        verify(memberService).saveMember(any(Member.class));
    }
}