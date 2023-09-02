package com.webnorm.prototypever1.auth;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class PasswordEncodeTest {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void 비밀번호_인코딩_테스트() {
        // given
        String encodedPassword = passwordEncoder.encode("1234");

        // when
        String givenPassword = "1234";

        // then
        assertThat(passwordEncoder.matches(givenPassword, encodedPassword)).isEqualTo(true);

    }
}
