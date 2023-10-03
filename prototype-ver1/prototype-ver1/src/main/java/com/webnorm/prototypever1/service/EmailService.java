package com.webnorm.prototypever1.service;

import com.webnorm.prototypever1.exception.exceptions.BusinessLogicException;
import com.webnorm.prototypever1.exception.exceptions.MemberException;
import com.webnorm.prototypever1.util.EmailMessage;
import com.webnorm.prototypever1.util.EmailType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.awt.*;
import java.util.Random;
import java.util.UUID;

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
    * 가입 환영 메일 : welcome, 임시 비밀번호 발송 메일 : password
    */
    public String sendMail(EmailMessage emailMessage, EmailType type) {
        String tmpPassword = null;
        if (type == EmailType.PASSWORD) // 임시 비밀번호 발송 메일인 경우 임시비밀번호 생성
            tmpPassword = createCode();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo());      // 메일 수신자 설정
            mimeMessageHelper.setSubject(emailMessage.getSubject());    // 메일 제목 설정
            mimeMessageHelper.setText(setContext(emailMessage.getName(), tmpPassword, type), true);     // 메일 본문, HTML 여부
            javaMailSender.send(mimeMessage);
            log.info("email sending success");
            return tmpPassword;
        } catch (MessagingException e) {
            log.info("email sending fail");
            throw new BusinessLogicException(MemberException.EMAIL_SEND_FAIL);
        }
    }

    /*
    * 이메일 인증코드 생성 메서드(미사용)
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
    * 임시 비밀번호 발송 메서드
    * */
    public String createTmpPassword() {
        Random random = new Random();
        StringBuffer tmpPassword = new StringBuffer();
        for (int i = 0; i < 12; i++) {
            int index = random.nextInt(4);
            switch (index) {
                case 0 : tmpPassword.append((char) ((int)random.nextInt(26) + 97)); break;
                case 1 : tmpPassword.append((char) ((int)random.nextInt(26) + 65)); break;
                default: tmpPassword.append((random.nextInt(9)));
            }
        }
        return tmpPassword.toString();
    }

    /*
     * thymeleaf 을 통한 html 적용
     */
    public String setContext(String name, String tmpPassword, EmailType type) {
        // thymeleaf value 설정
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("tmpPassword", tmpPassword);
        // welcomeEmail, passwordEmail 이라는 이름의 html 을 찾아 context 전달
        return templateEngine.process(type.getValue() + "Email", context);
    }
}
