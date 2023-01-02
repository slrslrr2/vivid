package com.vivid.dream.controller;

import com.vivid.dream.entity.MemberEntity;
import com.vivid.dream.repo.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class MemberController {

    private final MemberRepository memberRepository;

    /**
     * 멤버 조회
     * @return
     */
    @GetMapping("member")
    public List<MemberEntity> findAllMember() {
        return memberRepository.findAll();
    }

    /**
     * 회원가입
     * @return
     */
    @PostMapping("member")
    public MemberEntity signUp() {
        final MemberEntity member = MemberEntity.builder()
                .username("test_user@gmail.com")
                .name("test user")
                .build();
        return memberRepository.save(member);
    }
}