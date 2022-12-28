package com.sparta.posting.dto;

import com.sparta.posting.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetResponseDto {
    private final String username;
    private final String title;
    private final String contents;
    private final LocalDateTime modifiedAt;

    public GetResponseDto(Post post) {
        this.username = post.getUsername();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.modifiedAt = post.getModifiedAt();
    }
}
