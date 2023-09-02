package com.webnorm.prototypever1.exception;

import com.webnorm.prototypever1.exception.exceptions.MemberEmailDuplicateException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.security.SignatureException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /*
     * 회원가입 Controller : email 중복 exception
     */
    @ExceptionHandler(MemberEmailDuplicateException.class)
    public ResponseEntity<ErrorResponse> handleEmailDuplicateException(MemberEmailDuplicateException e) {
        final ErrorResponse response = ErrorResponse.builder
                (e.getCause(), HttpStatus.BAD_REQUEST, "중복된 이메일입니다.").build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /*
    * Spring Security 관련 Exception
    */
    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ErrorResponse> handleSignatureException(SignatureException e) {
        final ErrorResponse response = ErrorResponse.builder
                (e.getCause(), HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다.").build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
//
//    @ExceptionHandler(MalformedJwtException.class)
//    public ResponseEntity<ApiResponse> handleMalformedJwtException() {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("올바르지 않은 토큰입니다."));
//    }
//
//    @ExceptionHandler(ExpiredJwtException.class)
//    public ResponseEntity<ApiResponse> handleExpiredJwtException() {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("토큰이 만료되었습니다. 다시 로그인해주세요."));
//    }
}
