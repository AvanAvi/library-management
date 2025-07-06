package com.attsw.library.management.unit;

import com.attsw.library.management.controller.MemberHTMLController;
import com.attsw.library.management.service.BookService;
import com.attsw.library.management.entity.Book;
import static org.junit.jupiter.api.Assertions.*;
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
import org.mockito.ArgumentCaptor;

@ExtendWith(MockitoExtension.class)
class MemberHTMLControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MemberService memberService;
    
    @Mock
    private BookService bookService;

    private MemberHTMLController memberHTMLController;

    @BeforeEach
    void setUp() {
        memberHTMLController = new MemberHTMLController(memberService, bookService);
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
       
        Long memberId = 1L;
        Member member = new Member(memberId, "John Doe", "john@email.com");
        // Member has no borrowed books (empty list by default)
        
        when(memberService.findById(memberId)).thenReturn(member);
        
        mockMvc.perform(post("/members-web/delete/{id}", memberId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/members-web"));
        
        verify(memberService).findById(memberId); 
        verify(memberService).deleteMember(memberId);
       
    }
    
    @Test
    void testShowEditMemberForm() throws Exception {
        Long memberId = 1L;
        Member member = new Member(memberId, "Test Member", "test@email.com");
        
        when(memberService.findById(memberId)).thenReturn(member);
        
        mockMvc.perform(get("/members-web/edit/{id}", memberId))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-member"))
                .andExpect(model().attributeExists("member"))
                .andExpect(model().attribute("member", member));
        
        verify(memberService).findById(memberId);
    }

    @Test
    void testUpdateMemberForm() throws Exception {
        Long memberId = 1L;
        Member savedMember = new Member(memberId, "Updated Member", "updated@email.com");
        
        when(memberService.saveMember(any(Member.class))).thenReturn(savedMember);
        
        mockMvc.perform(post("/members-web/update/{id}", memberId)
                .param("name", "Updated Member")
                .param("email", "updated@email.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/members-web"));
        
        ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);
        verify(memberService).saveMember(memberCaptor.capture());
        Member capturedMember = memberCaptor.getValue();
        assertEquals(memberId, capturedMember.getId());
    }
    
    @Test
    void testUpdateMemberSetIdCall() {
        // Direct test of the controller method to verify setId behavior
        Long pathId = 99L;
        Member testMember = new Member();
        // Member starts with null ID
        assertNull(testMember.getId());
        
        when(memberService.saveMember(any(Member.class))).thenReturn(testMember);
        
        // Call the controller method directly
        String result = memberHTMLController.updateMember(pathId, testMember);
        
        // Verify the behavior
        assertEquals("redirect:/members-web", result);
        verify(memberService).saveMember(testMember);
        
        // Critical: the member must have the ID set
        // This will fail if setId(id) is removed by mutant
        assertEquals(pathId, testMember.getId());
    }
    
    @Test
    void testShowMemberBooks() throws Exception {
        Long memberId = 1L;
        Member member = new Member(memberId, "John Doe", "john@email.com");
        List<Book> borrowedBooks = Arrays.asList(
            new Book(1L, "Book 1", "Author 1", "111111111", 2023, "Test"),
            new Book(2L, "Book 2", "Author 2", "222222222", 2023, "Test")
        );
        member.setBorrowedBooks(borrowedBooks);
        
        when(memberService.findById(memberId)).thenReturn(member);
        
        mockMvc.perform(get("/members-web/books/{id}", memberId))
                .andExpect(status().isOk())
                .andExpect(view().name("member-books"))
                .andExpect(model().attributeExists("member"))
                .andExpect(model().attributeExists("borrowedBooks"))
                .andExpect(model().attribute("member", member))
                .andExpect(model().attribute("borrowedBooks", borrowedBooks));
        
        verify(memberService).findById(memberId);
    }

    @Test
    void testReturnBookFromMember() throws Exception {
        Long memberId = 1L;
        Long bookId = 2L;
        Member member = new Member(memberId, "John Doe", "john@email.com");
        Book book = new Book(bookId, "Test Book", "Test Author", "123456789", 2023, "Test");
        book.setBorrowedBy(member); // Initially borrowed by this member
        
        when(bookService.findById(bookId)).thenReturn(book);
        when(bookService.saveBook(any(Book.class))).thenReturn(book);
        
        mockMvc.perform(post("/members-web/return-book/{memberId}/{bookId}", memberId, bookId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/members-web/books/" + memberId));
        
        verify(bookService).findById(bookId);
        verify(bookService).saveBook(book);
        assertNull(book.getBorrowedBy());
    }

    @Test
    void testDeleteMemberWithBorrowedBooks() throws Exception {
        Long memberId = 1L;
        Member member = new Member(memberId, "John Doe", "john@email.com");
        
        Book book1 = new Book(1L, "Book 1", "Author 1", "111111111", 2023, "Test");
        Book book2 = new Book(2L, "Book 2", "Author 2", "222222222", 2023, "Test");
        book1.setBorrowedBy(member);
        book2.setBorrowedBy(member);
        member.setBorrowedBooks(Arrays.asList(book1, book2));
        
        when(memberService.findById(memberId)).thenReturn(member);
        
        mockMvc.perform(post("/members-web/delete/{id}", memberId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/members-web"));
        
        verify(memberService).findById(memberId);
        verify(bookService, times(2)).saveBook(any(Book.class)); 
        verify(memberService).deleteMember(memberId);
        assertNull(book1.getBorrowedBy());
        assertNull(book2.getBorrowedBy());
    }
}