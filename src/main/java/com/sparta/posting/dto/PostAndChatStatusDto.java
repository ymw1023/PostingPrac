package com.sparta.posting.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
public class PostAndChatStatusDto {
    private final String msg;
    private final int statusCode;
    private final List<PostAndChatDto> post;

    public PostAndChatStatusDto(String msg, HttpStatus status, List<PostAndChatDto> post) {
        this.msg = msg;
        this.statusCode = status.value();
        this.post = post;
    }
}
