package com.sparta.posting.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {
    private final String comment;

    public CommentDto(String comment) {
        this.comment = comment;
    }
}
