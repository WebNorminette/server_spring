package com.webnorm.prototypever1.service;

import com.webnorm.prototypever1.entity.Member;
import com.webnorm.prototypever1.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    /*
    * [회원가입]
    * # 아이디(이메일) 중복검사
    * # 휴대폰 인증
    * # 비밀번호 해싱
    */
    public Member createMember(Member member) {
        memberRepository.findByUserId(member.getUserId());
        return memberRepository.save(member);
    }

    // 회원목록 조회(관리자)
    public List<Member> findAllMember() {
        return memberRepository.findAll();
    }
}
