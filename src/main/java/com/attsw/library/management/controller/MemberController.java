package com.attsw.library.management.controller;

import com.attsw.library.management.entity.Member;
import com.attsw.library.management.entity.Book;
import com.attsw.library.management.dto.MemberDto;
import com.attsw.library.management.service.MemberService;
import com.attsw.library.management.exception.MemberNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/members")
@Transactional
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<MemberDto> saveMember(@Valid @RequestBody MemberDto memberDto) {
        Member member = convertToEntity(memberDto);
        Member savedMember = memberService.saveMember(member);
        MemberDto savedMemberDto = convertToDto(savedMember);
        return new ResponseEntity<>(savedMemberDto, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MemberDto> findById(@PathVariable Long id) {
        try {
            Member member = memberService.findById(id);
            MemberDto memberDto = convertToDto(member);
            return new ResponseEntity<>(memberDto, HttpStatus.OK);
        } catch (MemberNotFoundException e) { 
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping
    public ResponseEntity<List<MemberDto>> findAll() {
        List<Member> members = memberService.findAll();
        List<MemberDto> memberDtos = members.stream()
                .map(this::convertToDto)
                .toList();
        return new ResponseEntity<>(memberDtos, HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<MemberDto> updateMember(@PathVariable Long id, @Valid @RequestBody MemberDto memberDto) {
        memberDto.setId(id);
        Member member = convertToEntity(memberDto);
        Member updatedMember = memberService.saveMember(member);
        MemberDto updatedMemberDto = convertToDto(updatedMember);
        return new ResponseEntity<>(updatedMemberDto, HttpStatus.OK);
    }
    
    // Entity to DTO conversion
    private MemberDto convertToDto(Member member) {
        List<Long> borrowedBookIds = member.getBorrowedBooks().stream()
                .map(Book::getId)
                .toList();
        
        return new MemberDto(
            member.getId(),
            member.getName(),
            member.getEmail(),
            borrowedBookIds
        );
    }
    
    // DTO to Entity conversion
    private Member convertToEntity(MemberDto memberDto) {
        return new Member(
            memberDto.getId(),
            memberDto.getName(),
            memberDto.getEmail()
        );
    }
}