package com.webnorm.prototypever1.exception.exceptions;


import com.webnorm.prototypever1.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AddressException implements ExceptionCode {
    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "cannot find address"),
    DEFAULT_ADDRESS_DUP(HttpStatus.CONFLICT, "too many default address");

    private final HttpStatus status;
    private final String message;
}
