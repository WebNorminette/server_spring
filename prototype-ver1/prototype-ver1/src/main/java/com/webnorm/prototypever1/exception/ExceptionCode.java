package com.webnorm.prototypever1.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionCode {

    HttpStatus getStatus();
    String getMessage();

}