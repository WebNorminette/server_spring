package com.webnorm.prototypever1.exception.exceptions;

import com.webnorm.prototypever1.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthException implements ExceptionCode {

    INVALID_SIGN(HttpStatus.UNAUTHORIZED, "invalid token signature"),
    MALFORMED_TOKEN(HttpStatus.UNAUTHORIZED, "malformed token"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "expired token"),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "cannot find refresh token"),
    NO_AUTH_IN_TOKEN(HttpStatus.UNAUTHORIZED, "token has no authority"),
    OAUTH_CANOOT_FIND_USERNAME(HttpStatus.UNAUTHORIZED, "cannot find username from oauth2");

    private final HttpStatus status;
    private final String message;
}
