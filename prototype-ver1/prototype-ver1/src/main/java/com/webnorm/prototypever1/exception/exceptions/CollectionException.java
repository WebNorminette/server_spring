package com.webnorm.prototypever1.exception.exceptions;

import com.webnorm.prototypever1.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CollectionException implements ExceptionCode {
    COLLECTION_NAME_DUP(HttpStatus.BAD_REQUEST, "collection name duplicated"),
    COLLECTION_NOT_FOUND(HttpStatus.NOT_FOUND, "cannot find collection"),
    INVALID_COLLECTION_NAME(HttpStatus.BAD_REQUEST, "invalid collection name");

    private final HttpStatus status;
    private final String message;
}
