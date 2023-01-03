package com.sparta.posting.dto;

import com.sparta.posting.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetResponseDto {
    private final String username;
    private final String title;
    private final String contents;
    private final LocalDateTime createdAt;

    public GetResponseDto(Post post) {
        this.username = post.getUser().getUsername();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.createdAt = post.getModifiedAt();
    }
}
