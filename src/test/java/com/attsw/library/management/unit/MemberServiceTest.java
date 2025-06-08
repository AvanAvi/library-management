package com.attsw.library.management.unit;

import com.attsw.library.management.entity.Member;
import com.attsw.library.management.repository.MemberRepository;
import com.attsw.library.management.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.Arrays;
import java.util.List;
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
        
        Member member = new Member(null, "Avan Avi", "avan.avi@email.com");
        Member savedMember = new Member(1L, "Avan Avi", "avan.avi@email.com");
        
        when(memberRepository.save(member)).thenReturn(savedMember);
        
        Member result = memberService.saveMember(member);
        
        assertNotNull(result.getId());
        assertEquals("Avan Avi", result.getName());
        assertEquals("avan.avi@email.com", result.getEmail());
        verify(memberRepository).save(member);
    }
    
    @Test
    void testFindById() {
       
        Long memberId = 1L;
        Member member = new Member(memberId, "Avan Avi", "avan.avi@email.com");
        
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        
        Member result = memberService.findById(memberId);
        
        assertNotNull(result);
        assertEquals(memberId, result.getId());
        assertEquals("Avan Avi", result.getName());
        assertEquals("avan.avi@email.com", result.getEmail());
        verify(memberRepository).findById(memberId);
    }
    

	@Test
	void testFindAll() {
	   
	    Member member1 = new Member(1L, "Avan Avi", "avan.avi@email.com");
	    Member member2 = new Member(2L, "John Doe", "john.doe@email.com");
	    List<Member> members = Arrays.asList(member1, member2);
	    
	    when(memberRepository.findAll()).thenReturn(members);
	    
	    List<Member> result = memberService.findAll();
	    
	    assertNotNull(result);
	    assertEquals(2, result.size());
	    assertEquals("Avan Avi", result.get(0).getName());
	    assertEquals("John Doe", result.get(1).getName());
	    verify(memberRepository).findAll();
	}
	
	@Test
	void testDeleteMember() {
	    // RED 
	    Long memberId = 1L;
	    
	    memberService.deleteMember(memberId);
	    
	    verify(memberRepository).deleteById(memberId);
	}
	
	@Test
	void testFindByIdThrowsExceptionWhenNotFound() {
	    
	    Long nonExistentId = 999L;
	    
	    when(memberRepository.findById(nonExistentId)).thenReturn(Optional.empty());
	    
	    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
	        memberService.findById(nonExistentId);
	    });
	    
	    assertEquals("Member not found with id: " + nonExistentId, exception.getMessage());
	    verify(memberRepository).findById(nonExistentId);
	}
}