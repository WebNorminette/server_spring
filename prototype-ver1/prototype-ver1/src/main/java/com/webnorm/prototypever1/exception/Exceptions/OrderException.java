package com.webnorm.prototypever1.exception.Exceptions;

import com.webnorm.prototypever1.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OrderException implements ExceptionCode {
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문이 없어요");

    private final HttpStatus status;
    private final String message;
}
