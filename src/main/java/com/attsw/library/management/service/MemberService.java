package com.attsw.library.management.service;

import com.attsw.library.management.entity.Member;
import com.attsw.library.management.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.attsw.library.management.exception.MemberNotFoundException;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }
    
    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> 
          new MemberNotFoundException("Member not found with id: " + id));
    }
    
    public List<Member> findAll() {
        return memberRepository.findAll();
    }
    
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
    
}