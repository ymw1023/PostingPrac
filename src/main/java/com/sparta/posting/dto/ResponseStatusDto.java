package com.sparta.posting.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseStatusDto {
    private final String msg;
    private final int statusCode;

    public ResponseStatusDto(String msg, HttpStatus status) {
        this.msg = msg;
        this.statusCode = status.value();
    }
}
