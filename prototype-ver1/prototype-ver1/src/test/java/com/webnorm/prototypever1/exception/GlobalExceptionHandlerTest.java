package com.webnorm.prototypever1.exception;

import com.webnorm.prototypever1.exception.exceptions.MemberEmailDuplicateException;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GlobalExceptionHandlerTest {
    void 이메일중복_예외처리() {
        // given
        throw new MemberEmailDuplicateException();

        // when

    }
}