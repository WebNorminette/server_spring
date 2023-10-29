package com.webnorm.prototypever1.controller;

import com.webnorm.prototypever1.api.response.SingleResponse;
import com.webnorm.prototypever1.service.EmailService;
import com.webnorm.prototypever1.util.EmailMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/send-email")
@RestController
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    // 가입 환영 메일 전송
    // 
    @RequestMapping("/welcome")
    public SingleResponse sendWelcomeEmail(String to, String name) {
        EmailMessage emailMessage = EmailMessage.builder()
                .to(to)
                .name(name)
                .subject("Customer account confirmation")
                .build();
        emailService.sendMail(emailMessage, "welcome");

        return new SingleResponse(HttpStatus.OK, "successfully send email");
    }
}
