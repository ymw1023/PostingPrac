package com.sparta.posting.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {
    private final String comment;
    private final Long likeCount;

    public CommentDto(String comment, Long likeCount) {
        this.comment = comment;
        this.likeCount = likeCount;
    }
}
