package com.webnorm.prototypever1.exception.Exceptions;

import com.webnorm.prototypever1.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class BusinessLogicException extends RuntimeException {
    private ExceptionCode exceptionCode;

    public BusinessLogicException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
