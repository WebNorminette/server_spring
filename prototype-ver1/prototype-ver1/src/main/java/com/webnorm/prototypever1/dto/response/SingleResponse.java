package com.webnorm.prototypever1.dto.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SingleResponse<T> extends CommonResponse {
    T data;

    public SingleResponse(HttpStatus status, String message, T data) {
        super(status.toString(), message);
        this.data = data;
    }

    public SingleResponse(HttpStatus status, String message) {
        super(status.toString(), message);
    }
}
