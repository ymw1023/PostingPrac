package com.sparta.posting.dto;

import lombok.Getter;

@Getter
public class PostsRequestDto {
    private String username;
    private String title;
    private String contents;
    private String password;
}
