package com.webnorm.prototypever1.service;


import com.webnorm.prototypever1.entity.Birth;
import com.webnorm.prototypever1.entity.Member;
import com.webnorm.prototypever1.entity.Msc;
import com.webnorm.prototypever1.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Rollback(value = true)
class MemberServiceTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 회원가입() {
        // given
        Member member = Member.builder()
                .email("test@test.com")
                .lastName("홍")
                .firstName("길동")
                .password("1234")
                .build();

        // when
        Member savedMember = memberRepository.save(member);

        // then
        assertThat(member.getId()).isEqualTo(savedMember.getId());
    }

    @Test
    void 회원가입_중복검사() {
        // given
        Member member1 = Member.builder()
                .email("duptest").lastName("홍").firstName("길동").password("1234")
                .build();
    }

    /*@Test
    void 회원목록조회(){
        // given
        Member member1 = Member.builder()
                .userId("junkim2@42seoul.student.kr")
                .userName("유저1")
                .password("1234")
                .gender("남자")
                .phoneNumber("010-1234-1234")
                .birth(new Birth(2000, 1, 1))
                .build();
        Member member2 = Member.builder()
                .userId("junkim1@gmail.com")
                .password("1234")
                .phoneNumber("010-1234-1234")
                .birth(new Birth(2000, 1, 1))
                .userName("유저2")
                .gender("남자")
                .build();
        List<Member> members = new ArrayList<>();
        members.add(member1);
        members.add(member2);
        memberRepository.saveAll(members);

        // when
        List<Member> findMembers = memberRepository.findAll();

        // then
        assertThat(findMembers.size()).isEqualTo(2);
    }*/
}