package com.sparta.posting.entity;

import com.sparta.posting.dto.ChatRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Chat extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHAT_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String comments;

    @Column(nullable = false)
    private Long likeCount;

    public Chat(ChatRequestDto requestDto, Post post, User user) {
        this.comments = requestDto.getComments();
        this.post = post;
        this.user = user;
        this.likeCount = 0L;
    }

    public void like(Long like) {
        this.likeCount += like;
    }

    public void update(ChatRequestDto requestDto) {
        this.comments = requestDto.getComments();
    }
}
