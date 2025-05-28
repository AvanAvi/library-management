package com.attsw.library.management.service;

import com.attsw.library.management.entity.Member;
import com.attsw.library.management.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

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
    	return memberRepository.findById(id).orElse(null);
    }
    
    public List<Member> findAll() {
        return memberRepository.findAll();
    }
}