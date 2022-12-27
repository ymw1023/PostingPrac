package com.sparta.posting.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostingDto {
    private String username;
    private String title;
    private String contents;

    private LocalDateTime modifiedAt;

    public PostingDto(String username, String title, String contents, LocalDateTime modifiedAt) {
        this.username = username;
        this.title = title;
        this.contents = contents;
        this.modifiedAt = modifiedAt;
    }
}
