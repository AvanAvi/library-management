package com.attsw.library.management.controller;

import com.attsw.library.management.entity.Member;
import com.attsw.library.management.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller  
public class MemberHTMLController {

    private static final String REDIRECT_MEMBERS_WEB = "redirect:/members-web";
    
    private final MemberService memberService;

    @Autowired
    public MemberHTMLController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members-web")
    public String showMembersPage(Model model) {
        // Get all members from service
        List<Member> members = memberService.findAll();
        
        // Add members to model for Thymeleaf template
        model.addAttribute("members", members);
        
        // Return view name (members.html template)
        return "members";
    }

    @GetMapping("/members-web/new")
    public String showAddMemberForm(Model model) {
        // Create empty member object for form binding
        Member member = new Member();
        
        // Add member to model for form
        model.addAttribute("member", member);
        
        // Return add member form view
        return "add-member";
    }
    
    @PostMapping("/members-web")
    public String saveMember(@ModelAttribute Member member) {
        // Save member using service
        memberService.saveMember(member);
        
        // Redirect to members list (POST-Redirect-GET pattern)
        return REDIRECT_MEMBERS_WEB;
    }
    
    @PostMapping("/members-web/delete/{id}")
    public String deleteMember(@PathVariable Long id) {
        // Delete member using service
        memberService.deleteMember(id);
        
        // Redirect to members list
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
}