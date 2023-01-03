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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CHAT_ID")
    Long id;

    @Column(nullable = false)
    private String comments;

    @ManyToOne
    @JoinColumn(name = "POST_ID", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    public Chat(ChatRequestDto requestDto, Post post, User user) {
        this.comments = requestDto.getComments();
        this.post = post;
        this.user = user;
    }

    public void update(ChatRequestDto requestDto, User user) {
        this.comments = requestDto.getComments();
        this.user = user;
    }
}
