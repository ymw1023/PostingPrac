package com.sparta.posting.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class PostChatStatusDto {
    private final String msg;
    private final int statusCode;
    private final PostAndChatDto post;

    public PostChatStatusDto(String msg, HttpStatus status, PostAndChatDto post) {
        this.msg = msg;
        this.statusCode = status.value();
        this.post = post;
    }
}
