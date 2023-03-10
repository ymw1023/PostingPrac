package com.sparta.posting.dto;

import com.sparta.posting.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PostAndChatDto {
    private final String username;
    private final String title;
    private final String contents;
    private final LocalDateTime createdAt;
    private final Long likeCount;
    private final List<CommentDto> chats = new ArrayList<>();

    public PostAndChatDto(Post post, List<CommentDto> chats) {
        this.username = post.getUser().getUsername();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.createdAt = post.getCreatedAt();
        this.likeCount = post.getLikeCount();

        this.chats.addAll(chats);
    }
}
