package com.webnorm.prototypever1.api.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class MultiResponse<T> extends CommonResponse {
    List<T> dataList;

    public MultiResponse(HttpStatus status, String message, List<T> dataList) {
        super(status.toString(), message);
        this.dataList = dataList;
    }
}
