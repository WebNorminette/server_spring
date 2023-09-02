package com.webnorm.prototypever1.exception;

import com.webnorm.prototypever1.exception.Exceptions.AuthException;
import com.webnorm.prototypever1.exception.Exceptions.BusinessLogicException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.security.SignatureException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * BusinessException에서 발생한 에러
     *
     * @param ex BusinessException
     * @return ResponseEntity
     */

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomException(BusinessLogicException ex) {
        return ErrorResponseDto.toResponseEntity(ex.getExceptionCode());
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ErrorResponseDto> handleSignatureException() {
        return ErrorResponseDto.toResponseEntity(AuthException.INVALID_SIGN);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ErrorResponseDto> handleMalformedJwtException() {
        return ErrorResponseDto.toResponseEntity(AuthException.MALFORMED_TOKEN);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponseDto> handleExpiredJwtException() {
        return ErrorResponseDto.toResponseEntity(AuthException.EXPIRED_TOKEN);
    }
}