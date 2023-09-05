package com.webnorm.prototypever1.api.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
class CommonResponse {
    private LocalDateTime timeStamp = LocalDateTime.now();
    private String status;
    private String message;

    public CommonResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
