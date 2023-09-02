package com.webnorm.prototypever1.exception;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GlobalExceptionHandlerTest {
    void 이메일중복_예외처리() {
        // given
        throw new MemberEmailDuplicateException();

        // when

    }
}