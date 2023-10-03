package com.webnorm.prototypever1.exception.exceptions;

import com.webnorm.prototypever1.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberException implements ExceptionCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User Not Found"),
    EMAIL_DUP(HttpStatus.CONFLICT, "Email Duplicated!"),
    MAILKEY_MISMATCH(HttpStatus.CONFLICT, "Incorrect password."),
    INCORRECT_PASSWORD(HttpStatus.CONFLICT, "Password"),
    NOT_ACTIVE_USER(HttpStatus.FORBIDDEN, "This User Is Not Active"),
    EMAIL_PATTERN_INCORRECT(HttpStatus.BAD_REQUEST, "Incorrect email pattern"),
    PASSWORD_PATTERN_INCORRECT(HttpStatus.BAD_REQUEST, "Incorrect password pattern"),
    NAME_PATTERN_INCORRECT(HttpStatus.BAD_REQUEST, "Incorrect name pattern"),
    EMAIL_SEND_FAIL(HttpStatus.OK, "failed to send email");

    private final HttpStatus status;
    private final String message;
}