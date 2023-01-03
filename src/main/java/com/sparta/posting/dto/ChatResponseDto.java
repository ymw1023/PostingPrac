package com.sparta.posting.dto;

import com.sparta.posting.entity.Chat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatResponseDto {
    private final Long id;
    private final String username;
    private final String postTitle;
    private final String comments;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public ChatResponseDto(Chat chat) {
        this.id = chat.getId();
        this.username = chat.getUser().getUsername();
        this.postTitle = chat.getPost().getTitle();
        this.comments = chat.getComments();
        this.createdAt = chat.getCreatedAt();
        this.modifiedAt = chat.getModifiedAt();
    }
}
