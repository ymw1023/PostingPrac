package com.sparta.posting.dto;

import com.sparta.posting.entity.Posts;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostsCreateDto {
    private Long id;
    private String username;
    private String title;
    private String contents;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public PostsCreateDto(Posts post) {
        this.id = post.getId();
        this.username = post.getUsername();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.password = post.getPassword();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
    }
}
