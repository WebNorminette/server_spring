package com.webnorm.prototypever1.dto.response;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class MultiResponse<T> extends CommonResponse {
    List<T> dataList;
    Page<T> dataPage;

    public MultiResponse(HttpStatus status, String message, List<T> dataList) {
        super(status.toString(), message);
        this.dataList = dataList;
    }

    public MultiResponse(HttpStatus status, String message, Page<T> dataPage) {
        super(status.toString(), message);
        this.dataPage = dataPage;
    }
}
