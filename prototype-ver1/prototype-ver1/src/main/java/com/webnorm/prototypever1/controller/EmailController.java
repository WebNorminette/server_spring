package com.webnorm.prototypever1.controller;

import com.webnorm.prototypever1.dto.response.SingleResponse;
import com.webnorm.prototypever1.entity.member.Member;
import com.webnorm.prototypever1.exception.exceptions.BusinessLogicException;
import com.webnorm.prototypever1.exception.exceptions.MemberException;
import com.webnorm.prototypever1.security.oauth.SocialType;
import com.webnorm.prototypever1.service.EmailService;
import com.webnorm.prototypever1.service.MemberService;
import com.webnorm.prototypever1.util.EmailMessage;
import com.webnorm.prototypever1.util.EmailType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/email")
@RestController
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;
    private final MemberService memberService;

    // 가입 환영 메일 전송
    @GetMapping("/welcome")
    public SingleResponse sendWelcomeEmail(String to, String name) {
        EmailMessage emailMessage = EmailMessage.builder()
                .to(to)
                .name(name)
                .subject("Customer account confirmation")
                .build();
        emailService.sendMail(emailMessage, EmailType.WELCOME);

        return new SingleResponse(HttpStatus.OK, "successfully send welcome email");
    }

    // 임시 비밀번호 메일 전송 + 비밀번호 재설정
    @PostMapping("/password/{email}")
    public SingleResponse sendPasswordResetEmail(@PathVariable("email") String email) {
        // email 로 회원 조회
        Member member = memberService.findMemberByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(MemberException.USER_NOT_FOUND));
        // email 생성
        EmailMessage emailMessage = EmailMessage.builder()
                .to(email)
                .name(member.getName())
                .subject("Customer account password reset")
                .build();
        // email 전송 후 전송한 임시비번 받아오기
        String tempPassword = emailService.sendMail(emailMessage, EmailType.PASSWORD);
        // 임시비번 저장
        memberService.updatePassword(member.getId(), tempPassword);
        return new SingleResponse(HttpStatus.OK, "successfully send password reset email");
    }
}

