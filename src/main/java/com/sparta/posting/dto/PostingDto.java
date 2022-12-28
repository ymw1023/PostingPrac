package com.sparta.posting.dto;

import com.sparta.posting.entity.Posts;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostingDto {
    private String username;
    private String title;
    private String contents;

    private LocalDateTime modifiedAt;

    public PostingDto(Posts post) {
        this.username = post.getUsername();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.modifiedAt = post.getModifiedAt();
    }
}
