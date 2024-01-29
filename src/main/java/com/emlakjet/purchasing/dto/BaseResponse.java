package com.emlakjet.purchasing.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@Data
public class BaseResponse {

    private String message;
    private Object data;


    public BaseResponse(Object data) {
        this.data = data;
    }

    public BaseResponse(String message) {
        this.message = message;
    }

    public BaseResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }

}
