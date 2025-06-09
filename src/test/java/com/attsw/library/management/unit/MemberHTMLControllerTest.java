package com.attsw.library.management.unit;

import com.attsw.library.management.controller.MemberHTMLController;
import com.attsw.library.management.service.MemberService;
import com.attsw.library.management.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class MemberHTMLControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MemberService memberService;

    private MemberHTMLController memberHTMLController;

    @BeforeEach
    void setUp() {
        memberHTMLController = new MemberHTMLController(memberService);
        mockMvc = MockMvcBuilders.standaloneSetup(memberHTMLController).build();
    }

    @Test
    void testShowMembersPage() throws Exception {
       
        List<Member> members = Arrays.asList(
            new Member(1L, "John Doe", "john.doe@email.com"),
            new Member(2L, "Jane Smith", "jane.smith@email.com")
        );
        
        when(memberService.findAll()).thenReturn(members);
        
        mockMvc.perform(get("/members-web"))
                .andExpect(status().isOk())
                .andExpect(view().name("members"))
                .andExpect(model().attributeExists("members"))
                .andExpect(model().attribute("members", members));
        
        verify(memberService).findAll();
    }

    @Test
    void testShowAddMemberForm() throws Exception {
      
        mockMvc.perform(get("/members-web/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-member"))
                .andExpect(model().attributeExists("member"));
    }
    
    @Test
    void testSaveMember() throws Exception {
        // RED: Test for saving member via web form
        Member savedMember = new Member(1L, "John Doe", "john.doe@email.com");
        
        when(memberService.saveMember(any(Member.class))).thenReturn(savedMember);
        
        mockMvc.perform(post("/members-web")
                .param("name", "John Doe")
                .param("email", "john.doe@email.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/members-web"));
        
        verify(memberService).saveMember(any(Member.class));
    }
    
    @Test
    void testDeleteMember() throws Exception {
        // RED: Test for deleting member via web
        Long memberId = 1L;
        
        mockMvc.perform(post("/members-web/delete/{id}", memberId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/members-web"));
        
        verify(memberService).deleteMember(memberId);
    }
    
    @Test
    void testShowEditMemberForm() throws Exception {
        // ARRANGE
        Long memberId = 1L;
        Member member = new Member(memberId, "Test Member", "test@email.com");
        
        when(memberService.findById(memberId)).thenReturn(member);
        
        // ACT & ASSERT
        mockMvc.perform(get("/members-web/edit/{id}", memberId))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-member"))
                .andExpect(model().attributeExists("member"))
                .andExpect(model().attribute("member", member));
        
        verify(memberService).findById(memberId);
    }

    @Test
    void testUpdateMemberForm() throws Exception {
        // ARRANGE
        Long memberId = 1L;
        Member savedMember = new Member(memberId, "Updated Member", "updated@email.com");
        
        when(memberService.saveMember(any(Member.class))).thenReturn(savedMember);
        
        // ACT & ASSERT
        mockMvc.perform(post("/members-web/update/{id}", memberId)
                .param("name", "Updated Member")
                .param("email", "updated@email.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/members-web"));
        
        verify(memberService).saveMember(any(Member.class));
    }
    
   
}