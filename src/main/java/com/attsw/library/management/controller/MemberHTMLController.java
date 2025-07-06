package com.attsw.library.management.controller;

import com.attsw.library.management.entity.Member;
import com.attsw.library.management.entity.Book;
import com.attsw.library.management.service.MemberService;
import com.attsw.library.management.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Controller  
public class MemberHTMLController {

    private static final String REDIRECT_MEMBERS_WEB = "redirect:/members-web";
    
    private final MemberService memberService;
    private final BookService bookService;

    @Autowired
    public MemberHTMLController(MemberService memberService, BookService bookService) {
        this.memberService = memberService;
        this.bookService = bookService;
    }

    @GetMapping("/members-web")
    @Transactional(readOnly = true)
    public String showMembersPage(Model model) {
        List<Member> members = memberService.findAll();
        model.addAttribute("members", members);
        return "members";
    }

    @GetMapping("/members-web/new")
    public String showAddMemberForm(Model model) {
        Member member = new Member();
        model.addAttribute("member", member);
        return "add-member";
    }
    
    @PostMapping("/members-web")
    public String saveMember(@ModelAttribute Member member) {
        memberService.saveMember(member);
        return REDIRECT_MEMBERS_WEB;
    }
    
    @PostMapping("/members-web/delete/{id}")
    @Transactional
    public String deleteMember(@PathVariable Long id) {
        Member member = memberService.findById(id);
        List<Book> borrowedBooks = member.getBorrowedBooks();
        
        for (Book book : borrowedBooks) {
            book.setBorrowedBy(null);
            bookService.saveBook(book);
        }
        
        memberService.deleteMember(id);
        return REDIRECT_MEMBERS_WEB;
    }
    
    @GetMapping("/members-web/edit/{id}")
    public String showEditMemberForm(@PathVariable Long id, Model model) {
        Member member = memberService.findById(id);
        model.addAttribute("member", member);
        return "edit-member";
    }

    @PostMapping("/members-web/update/{id}")
    public String updateMember(@PathVariable Long id, @ModelAttribute Member member) {
        member.setId(id);
        memberService.saveMember(member);
        return REDIRECT_MEMBERS_WEB;
    }
    
    @GetMapping("/members-web/books/{id}")
    @Transactional(readOnly = true)
    public String showMemberBooks(@PathVariable Long id, Model model) {
        Member member = memberService.findById(id);
        // Force initialization of lazy collection within transaction
        List<Book> borrowedBooks = member.getBorrowedBooks();
        int borrowedBooksCount = borrowedBooks.size(); // This triggers the lazy loading
        
        model.addAttribute("member", member);
        model.addAttribute("borrowedBooks", borrowedBooks);
        model.addAttribute("borrowedBooksCount", borrowedBooksCount);
        
        return "member-books";
    }
    
    @PostMapping("/members-web/return-book/{memberId}/{bookId}")
    public String returnBookFromMember(@PathVariable Long memberId, @PathVariable Long bookId) {
        Book book = bookService.findById(bookId);
        book.setBorrowedBy(null);
        bookService.saveBook(book);
        
        return "redirect:/members-web/books/" + memberId;
    }
}