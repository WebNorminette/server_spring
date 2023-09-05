package com.webnorm.prototypever1.exception.Exceptions;

import com.webnorm.prototypever1.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthException implements ExceptionCode {

    INVALID_SIGN(HttpStatus.UNAUTHORIZED, "invalid token signature"),
    MALFORMED_TOKEN(HttpStatus.UNAUTHORIZED, "malformed token"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "expired token");

    private final HttpStatus status;
    private final String message;
}
