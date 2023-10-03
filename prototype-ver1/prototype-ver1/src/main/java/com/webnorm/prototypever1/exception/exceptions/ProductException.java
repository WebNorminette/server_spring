package com.webnorm.prototypever1.exception.exceptions;

import com.webnorm.prototypever1.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProductException implements ExceptionCode {
    PRODUCT_NAME_DUP(HttpStatus.BAD_REQUEST, "product name duplicated"),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "cannot find product"),
    FILE_INPUT_ERROR(HttpStatus.BAD_REQUEST, "cannot read file");

    private final HttpStatus status;
    private final String message;
}
