package com.sparta.posting.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ChatStatusDto {
    private final String msg;
    private final int statusCode;
    private final ChatResponseDto chat;

    public ChatStatusDto(String msg, HttpStatus status, ChatResponseDto chat) {
        this.msg = msg;
        this.chat = chat;
        this.statusCode = status.value();
    }
}
