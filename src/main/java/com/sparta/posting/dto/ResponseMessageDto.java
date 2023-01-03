package com.sparta.posting.dto;

import lombok.Getter;

@Getter
public class ResponseMessageDto {
    private final String msg;

    public ResponseMessageDto(String msg) {
        this.msg = msg;
    }
}
