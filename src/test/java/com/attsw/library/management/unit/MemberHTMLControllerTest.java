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
        // RED:  MemberHTMLController doesn't exist yet
        List<Member> members = Arrays.asList(
            new Member(1L, "John Doe", "john.doe@email.com"),
            new Member(2L, "Jane Smith", "jane.smith@email.com")
        );
        
        when(memberService.findAll()).thenReturn(members);
        
        mockMvc.perform(get("/members-web"))
                .andExpected(status().isOk())
                .andExpect(view().name("members"))
                .andExpect(model().attributeExists("members"))
                .andExpect(model().attribute("members", members));
        
        verify(memberService).findAll();
    }

    @Test
    void testShowAddMemberForm() throws Exception {
        // RED: Test for displaying add member form
        mockMvc.perform(get("/members-web/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-member"))
                .andExpect(model().attributeExists("member"));
    }

    
}