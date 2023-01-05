package com.sparta.posting.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PostStatusDto {
    private final String msg;
    private final int statusCode;
    private final PostResponseDto posts;

    public PostStatusDto(String msg, HttpStatus status, PostResponseDto postResponseDto) {
        this.msg = msg;
        this.statusCode = status.value();
        this.posts = postResponseDto;
    }
}
