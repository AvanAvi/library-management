package com.attsw.library.management.unit;

import com.attsw.library.management.controller.MemberController;
import com.attsw.library.management.entity.Member;
import com.attsw.library.management.dto.MemberDto;
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
import java.util.ArrayList;

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
        MemberDto memberDto = new MemberDto(null, "Avan Avi", "avan.avi@email.com", new ArrayList<>());
        Member savedMember = new Member(1L, "Avan Avi", "avan.avi@email.com");
        
        when(memberService.saveMember(any(Member.class))).thenReturn(savedMember);
        
        ResponseEntity<MemberDto> response = memberController.saveMember(memberDto);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        MemberDto savedMemberDto = response.getBody();
        assertNotNull(savedMemberDto);
        assertEquals(1L, savedMemberDto.getId());
        assertEquals("Avan Avi", savedMemberDto.getName());
        assertEquals("avan.avi@email.com", savedMemberDto.getEmail());
        verify(memberService).saveMember(any(Member.class));
    }

    @Test
    void testFindById() {
        Long memberId = 1L;
        Member member = new Member(memberId, "Avan Avi", "avan.avi@email.com");
        
        when(memberService.findById(memberId)).thenReturn(member);
        
        ResponseEntity<MemberDto> response = memberController.findById(memberId);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        MemberDto memberDto = response.getBody();
        assertNotNull(memberDto);
        assertEquals(memberId, memberDto.getId());
        assertEquals("Avan Avi", memberDto.getName());
        assertEquals("avan.avi@email.com", memberDto.getEmail());
        verify(memberService).findById(memberId);
    }
    
    @Test
    void testFindAll() {
        Member member1 = new Member(1L, "Avan Avi", "avan.avi@email.com");
        Member member2 = new Member(2L, "John Doe", "john.doe@email.com");
        List<Member> members = Arrays.asList(member1, member2);
        
        when(memberService.findAll()).thenReturn(members);
        
        ResponseEntity<List<MemberDto>> response = memberController.findAll();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<MemberDto> memberDtos = response.getBody();
        assertNotNull(memberDtos);
        assertEquals(2, memberDtos.size());
        assertEquals("Avan Avi", memberDtos.get(0).getName());
        assertEquals("John Doe", memberDtos.get(1).getName());
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
            .thenThrow(new MemberNotFoundException("Member not found with id: " + nonExistentId));

        ResponseEntity<MemberDto> response = memberController.findById(nonExistentId);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(memberService).findById(nonExistentId);
    }
    
    @Test
    void testUpdateMember() {
        Long memberId = 1L;
        MemberDto memberDto = new MemberDto(memberId, "New Name", "new@email.com", new ArrayList<>());
        Member updatedMember = new Member(memberId, "New Name", "new@email.com");
        
        when(memberService.saveMember(any(Member.class))).thenReturn(updatedMember);
        
        ResponseEntity<MemberDto> response = memberController.updateMember(memberId, memberDto);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        MemberDto updatedMemberDto = response.getBody();
        assertNotNull(updatedMemberDto);
        assertEquals(memberId, updatedMemberDto.getId());
        assertEquals("New Name", updatedMemberDto.getName());
        assertEquals("new@email.com", updatedMemberDto.getEmail());
        verify(memberService).saveMember(any(Member.class));
    }
    
    @Test
    void testFindByIdWithBorrowedBooks() {
        // Test the borrowed books stream mapping
        Long memberId = 1L;
        Long bookId1 = 2L;
        Long bookId2 = 3L;
        
        // Create books
        com.attsw.library.management.entity.Book book1 = 
            new com.attsw.library.management.entity.Book(bookId1, "Book 1", "Author 1", "ISBN1", 2021, "Cat1");
        com.attsw.library.management.entity.Book book2 = 
            new com.attsw.library.management.entity.Book(bookId2, "Book 2", "Author 2", "ISBN2", 2022, "Cat2");
        
        // Create member with borrowed books
        Member member = new Member(memberId, "John Doe", "john@email.com");
        member.getBorrowedBooks().add(book1);
        member.getBorrowedBooks().add(book2);
        
        when(memberService.findById(memberId)).thenReturn(member);
        
        ResponseEntity<MemberDto> response = memberController.findById(memberId);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        MemberDto memberDto = response.getBody();
        assertNotNull(memberDto);
        assertEquals(memberId, memberDto.getId());
        assertEquals("John Doe", memberDto.getName());
        assertEquals(2, memberDto.getBorrowedBookIds().size()); 
        assertTrue(memberDto.getBorrowedBookIds().contains(bookId1));
        assertTrue(memberDto.getBorrowedBookIds().contains(bookId2));
        verify(memberService).findById(memberId);
    }
}