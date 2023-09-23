package com.webnorm.prototypever1.exception.exceptions;

import com.webnorm.prototypever1.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CategoryException implements ExceptionCode {
    CATEGORY_NAME_DUP(HttpStatus.BAD_REQUEST, "category name duplicated"),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "cannot find category");

    private final HttpStatus status;
    private final String message;
}
