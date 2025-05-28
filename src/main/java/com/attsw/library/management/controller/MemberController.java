package com.attsw.library.management.controller;

import com.attsw.library.management.entity.Member;
import com.attsw.library.management.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Member> saveMember(@RequestBody Member member) {
        Member savedMember = memberService.saveMember(member);
        return new ResponseEntity<>(savedMember, HttpStatus.CREATED);
    }
}