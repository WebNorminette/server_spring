package com.webnorm.prototypever1.controller;

import com.webnorm.prototypever1.api.request.MemberSignupRequest;
import com.webnorm.prototypever1.api.response.MemberListResponse;
import com.webnorm.prototypever1.entity.Member;
import com.webnorm.prototypever1.repository.MemberRepository;
import com.webnorm.prototypever1.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    // 회원가입 : request(dto)를 member 엔티티에 매핑 후 저장
    @PostMapping
    public ResponseEntity<Member> signup(@RequestBody MemberSignupRequest request) {
        //System.out.println(request.getUserId());
        Member member = Member.builder()
                .userName(request.getUserName())
                .userId(request.getUserId())
                .password(request.getPassword())
                .gender(request.getGender())
                .birth(request.getBirth())
                .phoneNumber(request.getPhoneNumber())
                .marketingMessageConsent(request.getMarketingMessageConsent())
                .build();
       Member newMember = memberService.createMember(member);
       return new ResponseEntity<>(newMember, HttpStatus.OK);
    }

    // 회원목록 조회(관리자) : response(dto) 리스트를 stream을 이용해 member 리스트로 매핑
    @GetMapping
    public ResponseEntity<?> memberList() {
        List<Member> findMembers = memberRepository.findAll();
        List<MemberListResponse> collect = findMembers.stream()
                .map(m -> MemberListResponse.builder()
                        .userId(m.getUserId())
                        .userName(m.getUserName())
                        .phoneNumber(m.getPhoneNumber())
                        .birth(m.getBirth())
                        .gender(m.getGender())
                        .address(m.getAddress())
                        .point(m.getPoint())
                        .build())
                .collect(Collectors.toList());
        return new ResponseEntity<>(collect, HttpStatus.OK);
    }
}
