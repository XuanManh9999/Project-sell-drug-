package com.back_end.myProject.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponsePageable {
    private int statusCode;
    private String message;
    private Object data;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    public ResponsePageable(int statusCode, String message, Object data, long totalElements, int totalPages, int currentPage) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
    }
}
