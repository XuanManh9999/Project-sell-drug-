package com.back_end.myProject.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseCustom {
    private int code;
    private String message;
    private Object data;
    public ResponseCustom(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

}
