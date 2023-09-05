package com.webnorm.prototypever1.service;

import com.webnorm.prototypever1.util.EmailMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.awt.*;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    private final MemberService memberService;

    /*
    * 이메일 발송 메소드
    * 임시 비밀번호 발송, 인증 코드 발송, 가입 환영 메일 등으로 사용 가능
    * type 으로 메일 발송 목적을 구분
    * 가입 환영 메일 : welcome
    */
    public String sendMail(EmailMessage emailMessage, String type) {
        String authNum = createCode();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo());      // 메일 수신자 설정
            mimeMessageHelper.setSubject(emailMessage.getSubject());    // 메일 제목 설정
            mimeMessageHelper.setText(setContext(emailMessage.getName(), authNum, type), true);     // 메일 본문, HTML 여부
            javaMailSender.send(mimeMessage);
            log.info("email sending success");
            return authNum;
        } catch (MessagingException e) {
            log.info("email sending fail");
            throw new RuntimeException(e);
        }
    }

    /*
    * 임시 비밀번호 발송 메소드 (미사용)
    */
    public String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(4);
            switch (index) {
                case 0 : key.append((char) ((int)random.nextInt(26) + 97)); break;
                case 1 : key.append((char) ((int)random.nextInt(26) + 65)); break;
                default: key.append((random.nextInt(9)));
            }
        }
        return key.toString();
    }

    /*
     * thymeleaf 을 통한 html 적용
     */
    public String setContext(String name, String code, String type) {
        Context context = new Context();
        context.setVariable("name", name);
        return templateEngine.process(type + "Email", context);
    }
}
